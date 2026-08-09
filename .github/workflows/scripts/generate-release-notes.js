const axios = require('axios');
const { execSync } = require('child_process');
const fs = require('fs');

const ATLASSIAN_EMAIL = process.env.ATLASSIAN_EMAIL;
const ATLASSIAN_API_TOKEN = process.env.ATLASSIAN_API_TOKEN;
const JIRA_URL = process.env.JIRA_URL;

// Normalize CONFLUENCE_URL so it works whether the secret includes the
// trailing "/wiki" path or not, e.g.:
//   https://yourcompany.atlassian.net        -> https://yourcompany.atlassian.net/wiki
//   https://yourcompany.atlassian.net/wiki    -> https://yourcompany.atlassian.net/wiki
//   https://yourcompany.atlassian.net/wiki/   -> https://yourcompany.atlassian.net/wiki
const CONFLUENCE_URL = process.env.CONFLUENCE_URL
  .replace(/\/+$/, '')
  .replace(/\/wiki$/, '') + '/wiki';

const CONFLUENCE_SPACE_KEY = process.env.CONFLUENCE_SPACE_KEY;
const CONFLUENCE_PARENT_PAGE = process.env.CONFLUENCE_PARENT_PAGE;
const PROJECT_KEY = process.env.PROJECT_KEY;

// URL raíz del sitio (sin /wiki), usada solo para obtener el cloudId.
const SITE_URL = CONFLUENCE_URL.replace(/\/wiki$/, '');

const FROM_TAG = process.env.INPUT_FROM_TAG || '';
const TO_TAG = process.env.INPUT_TO_TAG;

// Create axios instance with auth
const atlassianAxios = axios.create({
  auth: {
    username: ATLASSIAN_EMAIL,
    password: ATLASSIAN_API_TOKEN
  }
});

async function getCommitsBetweenTags() {
  try {
    let gitCommand;

    if (!FROM_TAG) {
      gitCommand = `git log ${TO_TAG} --pretty=format:"%H|%s|%an|%ae|%ai"`;
    } else {
      gitCommand = `git log ${FROM_TAG}..${TO_TAG} --pretty=format:"%H|%s|%an|%ae|%ai"`;
    }

    const output = execSync(gitCommand, { encoding: 'utf-8' });
    const commits = output.trim().split('\n').filter(line => line.length > 0);

    return commits.map(line => {
      const [hash, subject, author, email, date] = line.split('|');
      return { hash, subject, author, email, date };
    });
  } catch (error) {
    console.error('Error getting commits:', error.message);
    return [];
  }
}

function extractJiraKeys(commits) {
  const jiraKeyRegex = new RegExp(`${PROJECT_KEY}-\\d+`, 'g');
  const issuesMap = new Map();

  commits.forEach(commit => {
    const matches = [...commit.subject.matchAll(jiraKeyRegex)];
    matches.forEach(match => {
      const key = match[0];
      if (!issuesMap.has(key)) {
        issuesMap.set(key, {
          key,
          commits: []
        });
      }
      issuesMap.get(key).commits.push(commit);
    });
  });

  return Array.from(issuesMap.values());
}

// Resolve a space key (e.g. "ENG") to its numeric space id, which the v2 API requires.
async function getConfluenceSpaceId(spaceKey) {
  try {
    const response = await atlassianAxios.get(`${CONFLUENCE_URL}/api/v2/spaces`, {
      params: {
        keys: spaceKey,
        limit: 1
      }
    });
    if (!response.data.results || response.data.results.length === 0) {
      throw new Error(`No space found for key "${spaceKey}"`);
    }
    return response.data.results[0].id;
  } catch (error) {
    console.error(`Error getting space ID for ${spaceKey}:`, error.response?.data?.errors?.[0]?.title || error.message);
    throw error;
  }
}

// Find a page by exact title within a space (space id, not key).
async function getPageIdByTitle(title, spaceId) {
  try {
    const response = await atlassianAxios.get(`${CONFLUENCE_URL}/api/v2/pages`, {
      params: {
        'space-id': spaceId,
        title,
        limit: 1
      }
    });
    return response.data.results && response.data.results.length > 0 ? response.data.results[0] : null;
  } catch (error) {
    console.error(`Error getting page ID for "${title}":`, error.response?.data?.errors?.[0]?.title || error.message);
    return null;
  }
}

// List direct child pages of a given page id.
async function getPagesByParent(parentPageId) {
  try {
    const response = await atlassianAxios.get(`${CONFLUENCE_URL}/api/v2/pages/${parentPageId}/children`, {
      params: {
        limit: 50
      }
    });
    return response.data.results || [];
  } catch (error) {
    console.error(`Error getting child pages:`, error.response?.data?.errors?.[0]?.title || error.message);
    return [];
  }
}

async function createConfluencePage(title, content, spaceId, parentPageId = null) {
  try {
    const payload = {
      spaceId,
      status: 'current',
      title,
      body: {
        representation: 'storage',
        value: content
      }
    };

    if (parentPageId) {
      payload.parentId = parentPageId;
    }

    const response = await atlassianAxios.post(
      `${CONFLUENCE_URL}/api/v2/pages`,
      payload
    );

    return response.data;
  } catch (error) {
    console.error(`Error creating Confluence page "${title}":`, error.response?.data?.errors?.[0]?.title || error.message);
    throw error;
  }
}

async function updateConfluencePage(pageId, title, content) {
  try {
    // Need the current version number before we can update.
    const current = await atlassianAxios.get(`${CONFLUENCE_URL}/api/v2/pages/${pageId}`);
    const version = current.data.version.number;

    const updatePayload = {
      id: pageId,
      status: 'current',
      title,
      body: {
        representation: 'storage',
        value: content
      },
      version: {
        number: version + 1,
        message: 'Updated by release notes automation'
      }
    };

    const updateResponse = await atlassianAxios.put(
      `${CONFLUENCE_URL}/api/v2/pages/${pageId}`,
      updatePayload
    );

    return updateResponse.data;
  } catch (error) {
    console.error(`Error updating Confluence page ${pageId}:`, error.response?.data?.errors?.[0]?.title || error.message);
    throw error;
  }
}

function escapeXml(value) {
  if (!value) return '';
  return String(value)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&apos;');
}

// Obtiene el cloudId del sitio a partir del dominio, vía el endpoint público de Atlassian.
async function getCloudId() {
  try {
    const response = await axios.get(`${SITE_URL}/_edge/tenant_info`);
    if (!response.data || !response.data.cloudId) {
      throw new Error('La respuesta no contiene cloudId');
    }
    return response.data.cloudId;
  } catch (error) {
    console.error('Error obteniendo el cloudId del sitio:', error.response?.status, error.message);
    throw error;
  }
}

// Construye un "smart link" de Jira en formato storage de Confluence: una
// tabla en vivo con las incidencias encontradas (tipo, clave, resumen,
// asignado, prioridad, estado y fecha de actualización), resuelta por
// Confluence directamente contra Jira cada vez que se ve la página.
function buildReleaseNotesContent(issueKeys, cloudId) {
  if (issueKeys.length === 0) {
    return '<p>No se encontraron incidencias de Jira para esta release.</p>';
  }

  const jqlQuery = `key in (${issueKeys.join(', ')}) ORDER BY created DESC`;
  const issuesUrl = `${JIRA_URL}/issues/?jql=${encodeURIComponent(jqlQuery)}`;

  const datasource = {
    id: 'd8b75300-dfda-4519-b6cd-e49abbd50401',
    parameters: {
      cloudId,
      jql: jqlQuery
    },
    views: [
      {
        type: 'table',
        properties: {
          columns: [
            { key: 'issuetype' },
            { key: 'key' },
            { key: 'summary' },
            { key: 'assignee' },
            { key: 'priority' },
            { key: 'status' },
            { key: 'updated' }
          ]
        }
      }
    ]
  };

  return `
    <p />
    <a href="${escapeXml(issuesUrl)}" data-card-appearance="block" data-datasource="${escapeXml(JSON.stringify(datasource))}">${escapeXml(issuesUrl)}</a>
  `;
}

async function main() {
  try {
    console.log(`\n📝 Generating Release Notes from ${FROM_TAG || 'initial commit'} to ${TO_TAG}\n`);

    // Get commits
    console.log('🔍 Fetching commits...');
    const commits = await getCommitsBetweenTags();
    console.log(`✅ Found ${commits.length} commits\n`);

    // Extract Jira keys
    console.log('🔎 Extracting Jira keys from commits...');
    const issues = extractJiraKeys(commits);
    console.log(`✅ Found ${issues.length} unique Jira issues\n`);

    if (issues.length === 0) {
      console.log('⚠️  No Jira issues found in commit messages');
      fs.writeFileSync('/tmp/release_notes_summary.txt', '⚠️  No Jira issues found in commits between tags.');
      return;
    }

    // Build release notes content
    console.log('📄 Building release notes content...');
    const cloudId = await getCloudId();
    const releaseNotesContent = buildReleaseNotesContent(issues.map(i => i.key), cloudId);

    // Get or create parent Release Notes page
    console.log(`\n📍 Setting up Confluence pages in space "${CONFLUENCE_SPACE_KEY}"...`);

    // Step 0: Resolve the space key to the numeric space id the v2 API requires
    const spaceId = await getConfluenceSpaceId(CONFLUENCE_SPACE_KEY);
    console.log(`✅ Resolved space "${CONFLUENCE_SPACE_KEY}" to ID: ${spaceId}`);

    // Step 1: Find the project page (Mobile, Front-End, or Back-End)
    console.log(`Looking for "${CONFLUENCE_PARENT_PAGE}" page...`);
    const projectPage = await getPageIdByTitle(CONFLUENCE_PARENT_PAGE, spaceId);

    if (!projectPage) {
      console.error(`❌ Project page "${CONFLUENCE_PARENT_PAGE}" not found in Confluence`);
      fs.writeFileSync('/tmp/release_notes_summary.txt', `❌ Error: Project page "${CONFLUENCE_PARENT_PAGE}" not found in Confluence. Make sure it exists.`);
      return;
    }
    const projectPageId = projectPage.id;
    console.log(`✅ Found "${CONFLUENCE_PARENT_PAGE}" page (ID: ${projectPageId})`);

    // Step 2: Find or create Release Notes page under the project
    console.log(`\nLooking for "📓Release Notes" page under "${CONFLUENCE_PARENT_PAGE}"...`);
    const childPages = await getPagesByParent(projectPageId);
    let releaseNotesPage = childPages.find(p => p.title === '📓Release Notes');
    let releaseNotesPageId;

    if (!releaseNotesPage) {
      console.log('Creating "📓Release Notes" page...');
      try {
        const newPage = await createConfluencePage(
          '📓Release Notes',
          '<p>Release notes for ' + CONFLUENCE_PARENT_PAGE + ' releases.</p>',
          spaceId,
          projectPageId
        );
        releaseNotesPageId = newPage.id;
        console.log(`✅ Created 📓Release Notes page (ID: ${releaseNotesPageId})`);
      } catch (error) {
        console.error('Failed to create 📓Release Notes page:', error.message);
        throw error;
      }
    } else {
      releaseNotesPageId = releaseNotesPage.id;
      console.log(`✅ 📓Release Notes page already exists (ID: ${releaseNotesPageId})`);
    }

    // Step 3: Create or update version-specific page
    console.log(`\nLooking for "Release ${TO_TAG}" page under "Release Notes"...`);
    const versionPageTitle = `Release ${TO_TAG}`;
    const versionPages = await getPagesByParent(releaseNotesPageId);
    let versionPage = versionPages.find(p => p.title === versionPageTitle);
    let versionPageId;

    if (!versionPage) {
      console.log(`Creating "${versionPageTitle}" page...`);
      try {
        // Los "smart links" de Jira (data-datasource) no siempre se resuelven
        // como tabla cuando se crea la página por primera vez vía API, pero sí
        // lo hacen al actualizarla. Por eso creamos con contenido provisional
        // y de inmediato actualizamos con el contenido real.
        const newVersionPage = await createConfluencePage(
          versionPageTitle,
          '<p>Generando contenido...</p>',
          spaceId,
          releaseNotesPageId
        );
        versionPageId = newVersionPage.id;
        console.log(`✅ Created "${versionPageTitle}" page (ID: ${versionPageId})`);

        await updateConfluencePage(versionPageId, versionPageTitle, releaseNotesContent);
        console.log(`✅ Filled in "${versionPageTitle}" page content`);
      } catch (error) {
        console.error(`Failed to create "${versionPageTitle}" page:`, error.message);
        throw error;
      }
    } else {
      versionPageId = versionPage.id;
      console.log(`Updating existing "${versionPageTitle}" page...`);
      try {
        await updateConfluencePage(versionPageId, versionPageTitle, releaseNotesContent);
        console.log(`✅ Updated "${versionPageTitle}" page`);
      } catch (error) {
        console.error(`Failed to update "${versionPageTitle}" page:`, error.message);
        throw error;
      }
    }

    // Summary
    const summary = `
✅ **Release Notes Generated Successfully**

- **Version:** ${TO_TAG}
- **From Tag:** ${FROM_TAG || 'Initial commit'}
- **Total Commits:** ${commits.length}
- **Issues Found:** ${issues.length}
- **Confluence Page:** [${versionPageTitle}](${CONFLUENCE_URL}/spaces/${CONFLUENCE_SPACE_KEY}/pages/${versionPageId})

**Issues in this release:**
${issues.map(i => `- ${i.key}`).join('\n')}
    `;

    fs.writeFileSync('/tmp/release_notes_summary.txt', summary);
    console.log(summary);

  } catch (error) {
    console.error('\n❌ Error generating release notes:', error.message);
    fs.writeFileSync('/tmp/release_notes_summary.txt', `❌ Error: ${error.message}`);
    process.exit(1);
  }
}

main();