package acekode.debtshare.presentation.googlealias

import acekode.debtshare.googleAuth.GoogleAccount
import acekode.debtshare.presentation.AliasValidation
import acekode.debtshare.ui.items.DebtshareAvatar
import acekode.debtshare.ui.items.DebtshareAvatarSize
import acekode.debtshare.ui.items.DebtshareButton
import acekode.debtshare.ui.items.DebtshareButtonSize
import acekode.debtshare.ui.items.DebtshareTextField
import acekode.debtshare.ui.items.DebtshareTextFieldVariant
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import acekode.debtshare.ui.utils.clearFocusOnTap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.alias_available
import debtshare.app.generated.resources.alias_hint
import debtshare.app.generated.resources.alias_placeholder
import debtshare.app.generated.resources.alias_public
import debtshare.app.generated.resources.arrow_left
import debtshare.app.generated.resources.check_circle
import debtshare.app.generated.resources.google_alias_continue
import debtshare.app.generated.resources.google_alias_subtitle
import debtshare.app.generated.resources.google_alias_terms_footer
import debtshare.app.generated.resources.google_alias_title
import debtshare.app.generated.resources.google_logo
import debtshare.app.generated.resources.suggestions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GoogleAliasScreen(
    account: GoogleAccount,
    onNavigateBack: () -> Unit,
    onContinueClick: () -> Unit,
) {
    val viewModel = koinViewModel<GoogleAliasViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val aliasValidation by viewModel.aliasValidation.collectAsStateWithLifecycle()

    val suggestedAliases = remember(account.displayName) {
        generateAliasSuggestions(account.displayName).toPersistentList()
    }

    GoogleAliasContent(
        account = account,
        uiState = uiState,
        aliasValidation = aliasValidation,
        suggestions = suggestedAliases,
        onAliasChange = viewModel::onAliasChange,
        onContinueClick = { alias ->
            viewModel.onContinueClick(alias)
            onContinueClick()
        },
        onBackClick = onNavigateBack,
    )
}

@Composable
fun GoogleAliasContent(
    account: GoogleAccount,
    uiState: GoogleAliasUiState,
    aliasValidation: AliasValidation,
    suggestions: ImmutableList<String>,
    onAliasChange: (String) -> Unit,
    onContinueClick: (alias: String) -> Unit,
    onBackClick: () -> Unit,
) {
    var alias by remember { mutableStateOf("") }
    val canContinue = alias.isNotEmpty() && !aliasValidation.isTaken && !uiState.isLoading

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DebtshareTheme.colors.background)
                .clearFocusOnTap()
                .safeDrawingPadding()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                Spacer(Modifier.height(32.dp))

                Icon(
                    painter = painterResource(Res.drawable.arrow_left),
                    contentDescription = null,
                    tint = DebtshareTheme.colors.textPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackClick() },
                )

                Spacer(Modifier.height(24.dp))

                GoogleAccountCard(account = account)

                Spacer(Modifier.height(32.dp))

                GoogleAliasTitle()

                Spacer(Modifier.height(32.dp))

                DebtshareTextField(
                    value = alias,
                    onValueChange = {
                        alias = it
                        onAliasChange(it)
                    },
                    variant = DebtshareTextFieldVariant.Alias,
                    label = stringResource(Res.string.alias_public),
                    placeholder = stringResource(Res.string.alias_placeholder),
                    helpText = if (!aliasValidation.isTaken) stringResource(Res.string.alias_hint) else null,
                    isAliasAvailable = aliasValidation.isAvailable,
                    aliasAvailableLabel = stringResource(Res.string.alias_available),
                    isAliasTaken = aliasValidation.isTaken,
                )

                Spacer(Modifier.height(16.dp))

                AliasSuggestions(
                    suggestions = suggestions,
                    onSuggestionClick = { suggestion ->
                        alias = suggestion
                        onAliasChange(suggestion)
                    },
                )

                uiState.errorMessage?.let { errorMessage ->
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        style = DebtshareTheme.typography.bodySmall,
                        color = DebtshareColors.Semantic.error,
                    )
                }
            }

            CtaSection(
                isLoading = uiState.isLoading,
                canContinue = canContinue,
                onContinueClick = { onContinueClick(alias) },
            )
        }
    }
}

@Composable
private fun GoogleAccountCard(
    account: GoogleAccount,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = DebtshareTheme.colors.card,
                shape = RoundedCornerShape(DebtshareTheme.radius.large),
            )
            .border(
                width = 1.dp,
                color = DebtshareTheme.colors.border,
                shape = RoundedCornerShape(DebtshareTheme.radius.large),
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        GoogleAvatarWithBadge(name = account.displayName ?: "")

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = account.displayName ?: "",
                style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = DebtshareTheme.colors.textPrimary,
            )
            Text(
                text = account.email ?: "",
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textSecondary,
            )
        }

        Icon(
            painter = painterResource(Res.drawable.check_circle),
            contentDescription = null,
            tint = DebtshareColors.Semantic.success,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun GoogleAvatarWithBadge(
    name: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.size(48.dp)) {
        DebtshareAvatar(
            name = name,
            size = DebtshareAvatarSize.Large,
        )
        Box(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .background(DebtshareTheme.colors.card)
                .padding(2.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.google_logo),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun GoogleAliasTitle(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.google_alias_title),
            style = DebtshareTheme.typography.displayMedium,
            color = DebtshareTheme.colors.textPrimary,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(Res.string.google_alias_subtitle),
            style = DebtshareTheme.typography.bodyLarge,
            color = DebtshareTheme.colors.textTertiary,
        )
    }
}

@Composable
private fun AliasSuggestions(
    suggestions: ImmutableList<String>,
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (suggestions.isEmpty()) return

    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.suggestions),
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textMuted,
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            suggestions.forEach { suggestion ->
                AliasSuggestionChip(
                    suggestion = suggestion,
                    onClick = { onSuggestionClick(suggestion) },
                )
            }
        }
    }
}

@Composable
private fun AliasSuggestionChip(
    suggestion: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "@$suggestion",
        modifier = modifier
            .border(
                width = 1.dp,
                color = DebtshareTheme.colors.border,
                shape = RoundedCornerShape(100.dp),
            )
            .clip(RoundedCornerShape(100.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        style = DebtshareTheme.typography.bodyMedium,
        color = DebtshareTheme.colors.textSecondary,
    )
}

@Composable
private fun CtaSection(
    isLoading: Boolean,
    canContinue: Boolean,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DebtshareButton(
            text = stringResource(Res.string.google_alias_continue),
            onClick = onContinueClick,
            modifier = Modifier.fillMaxWidth(),
            size = DebtshareButtonSize.Large,
            enabled = canContinue,
            loading = isLoading,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.google_alias_terms_footer),
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textMuted,
            textAlign = TextAlign.Center,
        )
    }
}

internal fun generateAliasSuggestions(displayName: String?): List<String> {
    if (displayName.isNullOrBlank()) return emptyList()
    val parts = displayName.trim().split(" ").filter { it.isNotBlank() }
    val first = parts.getOrNull(0)?.lowercase()?.filter { it.isLetterOrDigit() } ?: return emptyList()
    val last = parts.getOrNull(1)?.lowercase()?.filter { it.isLetterOrDigit() }
    return if (last != null) {
        listOf(
            "$first${last.take(1).uppercase()}",
            "${first}_${last.take(1)}",
            "${first.take(1)}${last}26",
        )
    } else {
        listOf(first)
    }
}

private class GoogleAliasUiStateProvider : PreviewParameterProvider<GoogleAliasUiState> {
    override val values = sequenceOf(
        GoogleAliasUiState.Idle,
        GoogleAliasUiState.Loading,
        GoogleAliasUiState.Error("Something went wrong. Please try again."),
    )
}

private val previewAccount = GoogleAccount(
    id = "preview",
    displayName = "Ana Gómez",
    email = "ana.gomez@gmail.com",
    photoUrl = null,
)

@DebtshareScreenPreview
@Composable
private fun GoogleAliasScreenPreview(
    @PreviewParameter(GoogleAliasUiStateProvider::class) uiState: GoogleAliasUiState,
) {
    DebtshareTheme(darkTheme = false) {
        GoogleAliasContent(
            account = previewAccount,
            uiState = uiState,
            aliasValidation = AliasValidation.Available,
            suggestions = generateAliasSuggestions(previewAccount.displayName).toPersistentList(),
            onAliasChange = {},
            onContinueClick = {},
            onBackClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun GoogleAliasScreenAliasTakenPreview() {
    DebtshareTheme(darkTheme = false) {
        GoogleAliasContent(
            account = previewAccount,
            uiState = GoogleAliasUiState.Idle,
            aliasValidation = AliasValidation.Taken(listOf("anaG_28", "ana.gomez")),
            suggestions = generateAliasSuggestions(previewAccount.displayName).toPersistentList(),
            onAliasChange = {},
            onContinueClick = {},
            onBackClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun GoogleAliasScreenDarkPreview(
    @PreviewParameter(GoogleAliasUiStateProvider::class) uiState: GoogleAliasUiState,
) {
    DebtshareTheme(darkTheme = true) {
        GoogleAliasContent(
            account = previewAccount,
            uiState = uiState,
            aliasValidation = AliasValidation.Available,
            suggestions = generateAliasSuggestions(previewAccount.displayName).toPersistentList(),
            onAliasChange = {},
            onContinueClick = {},
            onBackClick = {},
        )
    }
}
