package acekode.debtshare.presentation.home.groups.invitations

import acekode.debtshare.ui.items.DebtshareIcon
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import acekode.debtshare.utils.NativeShareTargets
import acekode.debtshare.utils.copyToClipboard
import acekode.debtshare.utils.showToast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.at_sign
import debtshare.app.generated.resources.check
import debtshare.app.generated.resources.close
import debtshare.app.generated.resources.copy
import debtshare.app.generated.resources.house
import debtshare.app.generated.resources.invitation_copied
import debtshare.app.generated.resources.invitation_copy
import debtshare.app.generated.resources.invitation_expires
import debtshare.app.generated.resources.invitation_link_header
import debtshare.app.generated.resources.invitation_matches_header
import debtshare.app.generated.resources.invitation_members_subtitle
import debtshare.app.generated.resources.invitation_not_found
import debtshare.app.generated.resources.invitation_not_found_link
import debtshare.app.generated.resources.invitation_regenerate
import debtshare.app.generated.resources.invitation_search_hint
import debtshare.app.generated.resources.invitation_share_header
import debtshare.app.generated.resources.invitation_status_in_group
import debtshare.app.generated.resources.invitation_status_invite
import debtshare.app.generated.resources.invitation_status_pending
import debtshare.app.generated.resources.invitation_tab_alias
import debtshare.app.generated.resources.invitation_tab_link
import debtshare.app.generated.resources.invitation_title
import debtshare.app.generated.resources.link
import debtshare.app.generated.resources.search
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InvitationScreen(
    onCloseClick: () -> Unit,
) {
    val viewModel = koinViewModel<InvitationViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val linkCopied by viewModel.linkCopied.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is InvitationUiState.Content -> InvitationContent(
            uiState = state,
            selectedTab = selectedTab,
            searchQuery = searchQuery,
            linkCopied = linkCopied,
            onTabSelected = viewModel::onTabSelected,
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            onCopyLink = viewModel::onCopyLink,
            onRegenerateLink = viewModel::onRegenerateLink,
            onInviteUser = viewModel::onInviteUser,
            onCloseClick = onCloseClick,
        )

        is InvitationUiState.Loading -> {}
    }
}

@Composable
private fun InvitationContent(
    uiState: InvitationUiState.Content,
    selectedTab: InvitationTab,
    searchQuery: String,
    linkCopied: Boolean,
    onTabSelected: (InvitationTab) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onCopyLink: () -> Unit,
    onRegenerateLink: () -> Unit,
    onInviteUser: (String) -> Unit,
    onCloseClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(DebtshareTheme.colors.background),
    ) {
        InvitationHeader(
            groupName = uiState.groupName,
            memberCount = uiState.memberCount,
            onCloseClick = onCloseClick,
        )
        InvitationTabBar(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
        )

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                InvitationTab.Link -> LinkTabContent(
                    invitationLink = uiState.invitationLink,
                    linkCopied = linkCopied,
                    onCopyLink = onCopyLink,
                    onRegenerateLink = onRegenerateLink,
                )

                InvitationTab.Alias -> AliasTabContent(
                    searchQuery = searchQuery,
                    searchResults = uiState.searchResults,
                    invitationLink = uiState.invitationLink,
                    onSearchQueryChanged = onSearchQueryChanged,
                    onInviteUser = onInviteUser,
                )
            }
        }
    }
}

@Composable
private fun InvitationHeader(
    groupName: String,
    memberCount: Int,
    onCloseClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        DebtshareIcon(icon = Res.drawable.house, tint = DebtshareColors.Neutral.n0)

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(Res.string.invitation_title, groupName),
                style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = DebtshareTheme.colors.textPrimary,
            )
            Text(
                text = stringResource(Res.string.invitation_members_subtitle, memberCount),
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textSecondary,
            )
        }

        DebtshareIcon(icon = Res.drawable.close, onClick = onCloseClick)
    }
}

@Composable
private fun InvitationTabBar(
    selectedTab: InvitationTab,
    onTabSelected: (InvitationTab) -> Unit,
) {
    val borderColor = DebtshareTheme.colors.neutralTintStrong

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .drawBehind {
                val y = size.height - 0.5.dp.toPx()
                drawLine(borderColor, Offset(0f, y), Offset(size.width, y), 0.5.dp.toPx())
            },
    ) {
        InvitationTabItem(
            label = stringResource(Res.string.invitation_tab_link),
            icon = Res.drawable.link,
            selected = selectedTab == InvitationTab.Link,
            onClick = { onTabSelected(InvitationTab.Link) },
            modifier = Modifier.weight(1f),
        )
        InvitationTabItem(
            label = stringResource(Res.string.invitation_tab_alias),
            icon = Res.drawable.search,
            selected = selectedTab == InvitationTab.Alias,
            onClick = { onTabSelected(InvitationTab.Alias) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun InvitationTabItem(
    label: String,
    icon: DrawableResource,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val indicatorColor by animateColorAsState(
        targetValue = if (selected) DebtshareColors.Brand.primary else Color.Transparent,
        animationSpec = tween(150),
    )
    val contentColor = if (selected) DebtshareColors.Brand.primary else DebtshareTheme.colors.textSecondary

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(indicatorColor, Offset(0f, y), Offset(size.width, y), strokeWidth)
            }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = label,
                style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = contentColor,
            )
        }
    }
}

@Composable
private fun LinkTabContent(
    invitationLink: String,
    linkCopied: Boolean,
    onCopyLink: () -> Unit,
    onRegenerateLink: () -> Unit,
) {
    val copiedMessage = stringResource(Res.string.invitation_copied)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 24.dp),
    ) {
        Text(
            text = stringResource(Res.string.invitation_link_header),
            style = DebtshareTheme.typography.labelUppercase,
            color = DebtshareTheme.colors.textTertiary,
        )

        Spacer(Modifier.height(8.dp))

        InvitationLinkRow(
            invitationLink = invitationLink,
            linkCopied = linkCopied,
            onCopyClick = {
                copyToClipboard(invitationLink)
                showToast(copiedMessage)
                onCopyLink()
            },
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.invitation_expires),
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textTertiary,
            )
            Text(
                text = stringResource(Res.string.invitation_regenerate),
                style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = DebtshareColors.Brand.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onRegenerateLink)
                    .padding(4.dp),
            )
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = stringResource(Res.string.invitation_share_header),
            style = DebtshareTheme.typography.labelUppercase,
            color = DebtshareTheme.colors.textTertiary,
        )

        Spacer(Modifier.height(16.dp))

        NativeShareTargets(text = invitationLink)
    }
}

@Composable
private fun InvitationLinkRow(
    invitationLink: String,
    linkCopied: Boolean,
    onCopyClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DebtshareTheme.colors.card, RoundedCornerShape(12.dp))
            .border(1.dp, DebtshareTheme.colors.neutralTintStrong, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.link),
            contentDescription = null,
            tint = DebtshareTheme.colors.textSecondary,
            modifier = Modifier.size(16.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = invitationLink,
            style = DebtshareTheme.typography.bodyMedium,
            color = DebtshareTheme.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(DebtshareColors.Brand.primary)
                .clickable(onClick = onCopyClick)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                painter = painterResource(
                    if (linkCopied) Res.drawable.check else Res.drawable.copy,
                ),
                contentDescription = null,
                tint = DebtshareColors.Neutral.n0,
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = stringResource(
                    if (linkCopied) Res.string.invitation_copied else Res.string.invitation_copy,
                ),
                style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = DebtshareColors.Neutral.n0,
            )
        }
    }
}

@Composable
private fun AliasTabContent(
    searchQuery: String,
    searchResults: ImmutableList<UserSearchResultUiModel>,
    invitationLink: String,
    onSearchQueryChanged: (String) -> Unit,
    onInviteUser: (String) -> Unit,
) {
    val copiedMessage = stringResource(Res.string.invitation_copied)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
    ) {
        AliasSearchField(
            query = searchQuery,
            onQueryChanged = onSearchQueryChanged,
        )

        Spacer(Modifier.height(16.dp))

        if (searchResults.isNotEmpty()) {
            Text(
                text = stringResource(Res.string.invitation_matches_header),
                style = DebtshareTheme.typography.labelUppercase,
                color = DebtshareTheme.colors.textTertiary,
            )

            Spacer(Modifier.height(8.dp))
        }

        LazyColumn {
            items(searchResults, key = { it.id }) { user ->
                UserSearchResultItem(
                    user = user,
                    onInviteClick = { onInviteUser(user.id) },
                )
            }
            item {
                Spacer(Modifier.height(16.dp))
                InvitationLinkFooter(
                    onClick = {
                        copyToClipboard(invitationLink)
                        showToast(copiedMessage)
                    },
                )
            }
        }
    }
}

@Composable
private fun AliasSearchField(
    query: String,
    onQueryChanged: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, DebtshareColors.Brand.primary, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.at_sign),
            contentDescription = null,
            tint = DebtshareTheme.colors.textSecondary,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = stringResource(Res.string.invitation_search_hint),
                    style = DebtshareTheme.typography.bodyMedium,
                    color = DebtshareTheme.colors.textTertiary,
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChanged,
                textStyle = DebtshareTheme.typography.bodyMedium.copy(
                    color = DebtshareTheme.colors.textPrimary,
                ),
                singleLine = true,
                cursorBrush = SolidColor(DebtshareColors.Brand.primary),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun UserSearchResultItem(
    user: UserSearchResultUiModel,
    onInviteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(user.avatarColor, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = user.name.take(2).uppercase(),
                style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = DebtshareColors.Neutral.n0,
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.name,
                style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = DebtshareTheme.colors.textPrimary,
            )
            Text(
                text = "${user.alias} · ${user.subtitle}",
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textSecondary,
            )
        }

        Spacer(Modifier.width(8.dp))

        UserStatusBadge(
            status = user.status,
            onInviteClick = onInviteClick,
        )
    }
}

@Composable
private fun UserStatusBadge(
    status: UserInvitationStatus,
    onInviteClick: () -> Unit,
) {
    when (status) {
        is UserInvitationStatus.Available -> {
            Text(
                text = stringResource(Res.string.invitation_status_invite),
                style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = DebtshareColors.Neutral.n0,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(DebtshareColors.Brand.primary)
                    .clickable(onClick = onInviteClick)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
            )
        }

        is UserInvitationStatus.InGroup -> {
            Row(
                modifier = Modifier
                    .border(1.dp, DebtshareTheme.colors.neutralTintStrong, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.check),
                    contentDescription = null,
                    tint = DebtshareTheme.colors.textSecondary,
                    modifier = Modifier.size(12.dp),
                )
                Text(
                    text = stringResource(Res.string.invitation_status_in_group),
                    style = DebtshareTheme.typography.bodySmall,
                    color = DebtshareTheme.colors.textSecondary,
                )
            }
        }

        is UserInvitationStatus.Pending -> {
            Text(
                text = stringResource(Res.string.invitation_status_pending),
                style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = DebtshareColors.Accent.mustardDark,
                modifier = Modifier
                    .border(1.dp, DebtshareColors.Accent.mustardDark, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            )
        }
    }
}

@Composable
private fun InvitationLinkFooter(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    DebtshareTheme.colors.neutralTintStrong.copy(alpha = 0.3f),
                    CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            DebtshareIcon(icon = Res.drawable.at_sign, tint = DebtshareTheme.colors.textSecondary)
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = stringResource(Res.string.invitation_not_found),
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textSecondary,
            )
            Text(
                text = stringResource(Res.string.invitation_not_found_link),
                style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = DebtshareColors.Brand.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onClick),
            )
        }
    }
}

@DebtshareScreenPreview
@Composable
private fun InvitationLinkTabPreview() {
    DebtshareTheme(darkTheme = false) {
        InvitationContent(
            uiState = previewContent(),
            selectedTab = InvitationTab.Link,
            searchQuery = "",
            linkCopied = false,
            onTabSelected = {},
            onSearchQueryChanged = {},
            onCopyLink = {},
            onRegenerateLink = {},
            onInviteUser = {},
            onCloseClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun InvitationAliasTabPreview() {
    DebtshareTheme(darkTheme = false) {
        InvitationContent(
            uiState = previewContent().copy(searchResults = previewUsers()),
            selectedTab = InvitationTab.Alias,
            searchQuery = "lucia",
            linkCopied = false,
            onTabSelected = {},
            onSearchQueryChanged = {},
            onCopyLink = {},
            onRegenerateLink = {},
            onInviteUser = {},
            onCloseClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun InvitationLinkTabDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        InvitationContent(
            uiState = previewContent(),
            selectedTab = InvitationTab.Link,
            searchQuery = "",
            linkCopied = true,
            onTabSelected = {},
            onSearchQueryChanged = {},
            onCopyLink = {},
            onRegenerateLink = {},
            onInviteUser = {},
            onCloseClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun InvitationAliasTabDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        InvitationContent(
            uiState = previewContent().copy(searchResults = previewUsers()),
            selectedTab = InvitationTab.Alias,
            searchQuery = "lucia",
            linkCopied = false,
            onTabSelected = {},
            onSearchQueryChanged = {},
            onCopyLink = {},
            onRegenerateLink = {},
            onInviteUser = {},
            onCloseClick = {},
        )
    }
}

private fun previewContent() = InvitationUiState.Content(
    groupName = "Piso Castellana 43",
    memberCount = 4,
    invitationLink = "debtshare.app/j/pisocast-a7x9",
    searchResults = persistentListOf(),
)

private fun previewUsers() = persistentListOf(
    UserSearchResultUiModel(
        id = "u1",
        name = "Lucía Marín",
        alias = "@lucia",
        subtitle = "3 grupos en común",
        avatarColor = DebtshareColors.Accent.violet,
        status = UserInvitationStatus.Available,
    ),
    UserSearchResultUiModel(
        id = "u2",
        name = "Lucía Bernal",
        alias = "@luciab",
        subtitle = "Debtshare",
        avatarColor = DebtshareColors.Accent.plum,
        status = UserInvitationStatus.InGroup,
    ),
    UserSearchResultUiModel(
        id = "u3",
        name = "Luciano Prat",
        alias = "@lucianop",
        subtitle = "1 grupo",
        avatarColor = DebtshareColors.Semantic.success,
        status = UserInvitationStatus.Pending,
    ),
)
