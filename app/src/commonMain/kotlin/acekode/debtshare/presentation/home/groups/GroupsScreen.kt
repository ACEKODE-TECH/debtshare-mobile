package acekode.debtshare.presentation.home.groups

import acekode.debtshare.ui.items.DebtshareButton
import acekode.debtshare.ui.items.DebtshareButtonSize
import acekode.debtshare.ui.items.DebtshareButtonVariant
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.groups_create_group
import debtshare.app.generated.resources.groups_empty_description
import debtshare.app.generated.resources.groups_empty_title
import debtshare.app.generated.resources.groups_join_with_code
import debtshare.app.generated.resources.groups_subtitle_empty
import debtshare.app.generated.resources.inbox
import debtshare.app.generated.resources.plus
import debtshare.app.generated.resources.tab_home
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GroupsScreen(
    onCreateGroupClick: () -> Unit,
    onJoinWithCodeClick: () -> Unit,
) {
    val viewModel = koinViewModel<GroupsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GroupsContent(
        uiState = uiState,
        onCreateGroupClick = onCreateGroupClick,
        onJoinWithCodeClick = onJoinWithCodeClick,
    )
}

@Composable
private fun GroupsContent(
    uiState: GroupsUiState,
    onCreateGroupClick: () -> Unit,
    onJoinWithCodeClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(16.dp))

        GroupsHeader(uiState = uiState)

        when (uiState) {
            is GroupsUiState.Empty -> {
                GroupsEmptyBody(
                    onCreateGroupClick = onCreateGroupClick,
                    onJoinWithCodeClick = onJoinWithCodeClick,
                    modifier = Modifier.weight(1f),
                )
            }

            is GroupsUiState.Loading -> Spacer(Modifier.weight(1f))

            is GroupsUiState.Error -> Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun GroupsHeader(uiState: GroupsUiState) {
    val subtitle = when (uiState) {
        is GroupsUiState.Empty -> stringResource(Res.string.groups_subtitle_empty)
        is GroupsUiState.Loading -> ""
        is GroupsUiState.Error -> ""
    }

    Column {
        Text(
            text = stringResource(Res.string.tab_home),
            style = DebtshareTheme.typography.displayMedium,
            color = DebtshareTheme.colors.textPrimary,
        )

        if (subtitle.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = DebtshareTheme.typography.bodyMedium,
                color = DebtshareTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun GroupsEmptyBody(
    onCreateGroupClick: () -> Unit,
    onJoinWithCodeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.groups_empty_title),
            style = DebtshareTheme.typography.displaySmall,
            color = DebtshareTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.groups_empty_description),
            style = DebtshareTheme.typography.bodyMedium,
            color = DebtshareTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(32.dp))

        DebtshareButton(
            text = stringResource(Res.string.groups_create_group),
            onClick = onCreateGroupClick,
            icon = Res.drawable.plus,
            size = DebtshareButtonSize.Large,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(12.dp))

        DebtshareButton(
            text = stringResource(Res.string.groups_join_with_code),
            onClick = onJoinWithCodeClick,
            variant = DebtshareButtonVariant.Secondary,
            icon = Res.drawable.inbox,
            size = DebtshareButtonSize.Large,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private class GroupsUiStateProvider : PreviewParameterProvider<GroupsUiState> {
    override val values = sequenceOf(
        GroupsUiState.Empty,
        GroupsUiState.Loading,
        GroupsUiState.Error("Something went wrong"),
    )
}

@DebtshareScreenPreview
@Composable
private fun GroupsScreenPreview(
    @PreviewParameter(GroupsUiStateProvider::class) uiState: GroupsUiState,
) {
    DebtshareTheme(darkTheme = false) {
        GroupsContent(
            uiState = uiState,
            onCreateGroupClick = {},
            onJoinWithCodeClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun GroupsScreenDarkModePreview(
    @PreviewParameter(GroupsUiStateProvider::class) uiState: GroupsUiState,
) {
    DebtshareTheme(darkTheme = true) {
        GroupsContent(
            uiState = uiState,
            onCreateGroupClick = {},
            onJoinWithCodeClick = {},
        )
    }
}
