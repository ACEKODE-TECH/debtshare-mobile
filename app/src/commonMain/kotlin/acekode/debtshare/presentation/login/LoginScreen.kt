package acekode.debtshare.presentation.login

import acekode.debtshare.auth.ContinueWithGoogleButton
import acekode.debtshare.auth.GoogleAccount
import acekode.debtshare.auth.GoogleSignInResult
import acekode.debtshare.ui.items.DebtshareButton
import acekode.debtshare.ui.items.DebtshareButtonSize
import acekode.debtshare.ui.items.DebtshareTextField
import acekode.debtshare.ui.items.DebtshareTextFieldVariant
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import acekode.debtshare.ui.utils.clearFocusOnTap
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.create_account
import debtshare.app.generated.resources.email
import debtshare.app.generated.resources.email_placeholder
import debtshare.app.generated.resources.forgot_password
import debtshare.app.generated.resources.logo
import debtshare.app.generated.resources.no_account
import debtshare.app.generated.resources.or_with_email
import debtshare.app.generated.resources.password
import debtshare.app.generated.resources.password_placeholder
import debtshare.app.generated.resources.plus
import debtshare.app.generated.resources.remember_me
import debtshare.app.generated.resources.sign_in
import debtshare.app.generated.resources.welcome_back
import debtshare.app.generated.resources.welcome_subtitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToGoogleAlias: (GoogleAccount) -> Unit,
) {
    val viewModel = koinViewModel<LoginViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    with(uiState) {
        LoginContent(
            isLoading = this is LoginUiState.Loading,
            errorMessage = (this as? LoginUiState.Error)?.message,
            onLoginClick = viewModel::onLoginClick,
            onGoogleSignIn = { result ->
                viewModel.onGoogleSignIn(result)
                if (result is GoogleSignInResult.Success) {
                    onNavigateToGoogleAlias(result.account)
                }
            },
            onForgotPasswordClick = viewModel::onForgotPasswordClick,
            onCreateAccountClick = onNavigateToSignUp,
        )
    }
}

@Composable
fun LoginContent(
    isLoading: Boolean,
    errorMessage: String?,
    onLoginClick: (email: String, password: String, rememberMe: Boolean) -> Unit,
    onGoogleSignIn: (GoogleSignInResult) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPaddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DebtshareTheme.colors.background)
                .clearFocusOnTap()
                .verticalScroll(rememberScrollState())
                .safeDrawingPadding()
                .padding(innerPaddings)
                .padding(16.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            DebtshareLogo()

            Spacer(Modifier.height(32.dp))

            WelcomeTitle()

            Spacer(Modifier.height(32.dp))

            ContinueWithGoogleOrEmail(
                onGoogleSignIn = onGoogleSignIn,
            )

            EmailAndPasswordForm(
                email = email,
                password = password,
                rememberMe = rememberMe,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onRememberMeChange = { rememberMe = it },
                onForgotPasswordClick = onForgotPasswordClick,
            )

            Spacer(Modifier.height(32.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    style = DebtshareTheme.typography.bodySmall,
                    color = DebtshareColors.Semantic.error,
                )
                Spacer(Modifier.height(8.dp))
            }

            DebtshareButton(
                text = stringResource(Res.string.sign_in),
                onClick = { onLoginClick(email, password, rememberMe) },
                modifier = Modifier
                    .fillMaxWidth(),
                size = DebtshareButtonSize.Large,
                enabled = !isLoading,
                loading = isLoading,
            )

            Spacer(Modifier.height(16.dp))

            CreateAccount(
                modifier = Modifier
                    .fillMaxWidth(),
                onCreateAccountClick = onCreateAccountClick,
            )
        }
    }
}

@Composable
private fun DebtshareLogo(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(DebtshareTheme.radius.large)),
        ) {
            Icon(
                painter = painterResource(Res.drawable.logo),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(48.dp),
            )
        }
        Text(
            text = "Debtshare",
            style = DebtshareTheme.typography.displayLarge,
            color = DebtshareTheme.colors.textPrimary,
        )
    }
}

@Composable
private fun WelcomeTitle(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(Res.string.welcome_back),
            style = DebtshareTheme.typography.displayMedium,
            color = DebtshareTheme.colors.textPrimary,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(Res.string.welcome_subtitle),
            style = DebtshareTheme.typography.bodyLarge,
            color = DebtshareTheme.colors.textTertiary,
        )
    }
}

@Composable
private fun ContinueWithGoogleOrEmail(
    onGoogleSignIn: (GoogleSignInResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        ContinueWithGoogleButton(
            onResult = onGoogleSignIn,
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.medium),
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = DebtshareTheme.colors.border,
            )
            Text(
                text = stringResource(Res.string.or_with_email),
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textMuted,
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = DebtshareTheme.colors.border,
            )
        }
    }
}

@Composable
private fun EmailAndPasswordForm(
    email: String,
    password: String,
    rememberMe: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(Modifier.height(16.dp))

        DebtshareTextField(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(Res.string.email),
            placeholder = stringResource(Res.string.email_placeholder),
        )

        Spacer(Modifier.height(16.dp))

        DebtshareTextField(
            value = password,
            onValueChange = onPasswordChange,
            variant = DebtshareTextFieldVariant.Password,
            label = stringResource(Res.string.password),
            placeholder = stringResource(Res.string.password_placeholder),
        )

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { onRememberMeChange(!rememberMe) },
                ),
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = DebtshareColors.Brand.primary,
                    ),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(Res.string.remember_me),
                    style = DebtshareTheme.typography.bodyMedium,
                    color = DebtshareTheme.colors.textSecondary,
                )
            }
            Text(
                text = stringResource(Res.string.forgot_password),
                style = DebtshareTheme.typography.bodyMedium,
                color = DebtshareColors.Brand.primary,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onForgotPasswordClick,
                ),
            )
        }
    }
}

@Composable
private fun CreateAccount(
    onCreateAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.no_account) + " ",
            style = DebtshareTheme.typography.bodyMedium,
            color = DebtshareTheme.colors.textTertiary,
        )
        Text(
            text = stringResource(Res.string.create_account),
            style = DebtshareTheme.typography.bodyMedium,
            color = DebtshareColors.Brand.primary,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onCreateAccountClick,
            ),
        )
    }
}

private class LoginUiStateProvider : PreviewParameterProvider<LoginUiState> {
    override val values = sequenceOf(
        LoginUiState.Idle,
        LoginUiState.Loading,
        LoginUiState.Error("Invalid email or password."),
    )
}

@DebtshareScreenPreview
@Composable
private fun LoginScreenPreview(
    @PreviewParameter(LoginUiStateProvider::class) uiState: LoginUiState,
) {
    DebtshareTheme(darkTheme = false) {
        LoginContent(
            isLoading = uiState is LoginUiState.Loading,
            errorMessage = (uiState as? LoginUiState.Error)?.message,
            onLoginClick = { _, _, _ -> },
            onGoogleSignIn = {},
            onForgotPasswordClick = {},
            onCreateAccountClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun LoginScreenDarkPreview(
    @PreviewParameter(LoginUiStateProvider::class) uiState: LoginUiState,
) {
    DebtshareTheme(darkTheme = true) {
        LoginContent(
            isLoading = uiState is LoginUiState.Loading,
            errorMessage = (uiState as? LoginUiState.Error)?.message,
            onLoginClick = { _, _, _ -> },
            onGoogleSignIn = {},
            onForgotPasswordClick = {},
            onCreateAccountClick = {},
        )
    }
}
