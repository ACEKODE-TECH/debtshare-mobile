package acekode.debtshare.presentation.signup

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.alias
import debtshare.app.generated.resources.alias_available
import debtshare.app.generated.resources.alias_hint
import debtshare.app.generated.resources.alias_placeholder
import debtshare.app.generated.resources.alias_taken_prefix
import debtshare.app.generated.resources.already_have_account
import debtshare.app.generated.resources.arrow_left
import debtshare.app.generated.resources.close
import debtshare.app.generated.resources.create_account
import debtshare.app.generated.resources.email
import debtshare.app.generated.resources.email_placeholder
import debtshare.app.generated.resources.password
import debtshare.app.generated.resources.password_placeholder
import debtshare.app.generated.resources.password_strength_fair
import debtshare.app.generated.resources.password_strength_strong
import debtshare.app.generated.resources.password_strength_very_strong
import debtshare.app.generated.resources.password_strength_weak
import debtshare.app.generated.resources.privacy_policy_link
import debtshare.app.generated.resources.sign_in
import debtshare.app.generated.resources.signup_subtitle
import debtshare.app.generated.resources.signup_title
import debtshare.app.generated.resources.terms_accept_prefix
import debtshare.app.generated.resources.terms_and_conjunction
import debtshare.app.generated.resources.terms_link
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(
    onNavigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<SignUpViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val aliasValidation by viewModel.aliasValidation.collectAsStateWithLifecycle()

    with(uiState) {
        SignUpContent(
            isLoading = this is SignUpUiState.Loading,
            errorMessage = (this as? SignUpUiState.Error)?.message,
            isAliasAvailable = aliasValidation is AliasValidation.Available,
            isAliasTaken = aliasValidation is AliasValidation.Taken,
            aliasSuggestions = (aliasValidation as? AliasValidation.Taken)?.suggestions.orEmpty().toPersistentList(),
            onSignUpClick = viewModel::onSignUpClick,
            onAliasChange = viewModel::onAliasChange,
            onLoginClick = onNavigateBack,
            onTermsClick = viewModel::onTermsClick,
            onPrivacyPolicyClick = viewModel::onPrivacyPolicyClick,
            onBackClick = onNavigateBack,
        )
    }
}

@Composable
fun SignUpContent(
    isLoading: Boolean,
    errorMessage: String?,
    isAliasAvailable: Boolean,
    isAliasTaken: Boolean,
    aliasSuggestions: ImmutableList<String>,
    onSignUpClick: (alias: String, email: String, password: String, termsAccepted: Boolean) -> Unit,
    onAliasChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    var alias by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }

    val passwordStrength = remember(password) { computePasswordStrength(password) }
    val canSignUp = !isAliasTaken && termsAccepted && !isLoading

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
            Spacer(Modifier.height(32.dp))

            Icon(
                painter = painterResource(Res.drawable.arrow_left),
                contentDescription = null,
                tint = DebtshareTheme.colors.textPrimary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onBackClick()
                    },
            )

            Spacer(Modifier.height(40.dp))

            SignUpTitle()

            Spacer(Modifier.height(32.dp))

            AliasField(
                value = alias,
                onValueChange = {
                    alias = it
                    onAliasChange(it)
                },
                isAvailable = isAliasAvailable,
                isTaken = isAliasTaken,
                suggestions = aliasSuggestions,
            )

            Spacer(Modifier.height(16.dp))

            DebtshareTextField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(Res.string.email),
                placeholder = stringResource(Res.string.email_placeholder),
            )

            Spacer(Modifier.height(16.dp))

            SignUpPasswordField(
                value = password,
                onValueChange = { password = it },
                strength = passwordStrength,
            )

            Spacer(Modifier.height(32.dp))

            TermsCheckbox(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it },
                onTermsClick = onTermsClick,
                onPrivacyPolicyClick = onPrivacyPolicyClick,
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
                text = stringResource(Res.string.create_account),
                onClick = { onSignUpClick(alias, email, password, termsAccepted) },
                modifier = Modifier.fillMaxWidth(),
                size = DebtshareButtonSize.Large,
                enabled = canSignUp,
                loading = isLoading,
            )

            Spacer(Modifier.height(16.dp))

            AlreadyHaveAccount(
                modifier = Modifier.fillMaxWidth(),
                onLoginClick = onLoginClick,
            )
        }
    }
}

@Composable
private fun SignUpTitle(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.signup_title),
            style = DebtshareTheme.typography.displayMedium,
            color = DebtshareTheme.colors.textPrimary,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(Res.string.signup_subtitle),
            style = DebtshareTheme.typography.bodyLarge,
            color = DebtshareTheme.colors.textTertiary,
        )
    }
}

@Composable
private fun AliasField(
    value: String,
    onValueChange: (String) -> Unit,
    isAvailable: Boolean,
    isTaken: Boolean,
    suggestions: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DebtshareTextField(
            value = value,
            onValueChange = onValueChange,
            variant = DebtshareTextFieldVariant.Alias,
            label = stringResource(Res.string.alias),
            placeholder = stringResource(Res.string.alias_placeholder),
            helpText = if (!isTaken) stringResource(Res.string.alias_hint) else null,
            isAliasAvailable = isAvailable,
            aliasAvailableLabel = stringResource(Res.string.alias_available),
            isAliasTaken = isTaken,
        )
        if (isTaken) {
            Spacer(Modifier.height(DebtshareTheme.spacing.verySmall))
            AliasTakenError(suggestions = suggestions)
        }
    }
}

@Composable
private fun AliasTakenError(
    suggestions: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    val linkColor = DebtshareColors.Brand.primary
    val errorColor = DebtshareColors.Semantic.error
    val baseText = stringResource(Res.string.alias_taken_prefix)

    val annotatedText = buildAnnotatedString {
        withStyle(SpanStyle(color = errorColor)) {
            append("$baseText ")
        }
        suggestions.forEachIndexed { index, suggestion ->
            withStyle(SpanStyle(color = linkColor, fontWeight = FontWeight.SemiBold)) {
                append("@$suggestion")
            }
            if (index < suggestions.lastIndex) {
                withStyle(SpanStyle(color = errorColor)) {
                    append(" o ")
                }
            }
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = painterResource(Res.drawable.close),
            contentDescription = null,
            tint = errorColor,
            modifier = Modifier
                .size(14.dp)
                .padding(top = 2.dp),
        )
        Text(
            text = annotatedText,
            style = DebtshareTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun SignUpPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    strength: PasswordStrength,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DebtshareTextField(
            value = value,
            onValueChange = onValueChange,
            variant = DebtshareTextFieldVariant.Password,
            label = stringResource(Res.string.password),
            placeholder = stringResource(Res.string.password_placeholder),
        )
        if (strength != PasswordStrength.None) {
            Spacer(Modifier.height(8.dp))
            PasswordStrengthBar(strength = strength)
        }
    }
}

@Composable
private fun PasswordStrengthBar(
    strength: PasswordStrength,
    modifier: Modifier = Modifier,
) {
    val filledBars = strength.barCount
    val barColor = strength.color
    val label = stringResource(strength.labelRes)

    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            color = if (index < filledBars) barColor else DebtshareTheme.colors.border,
                            shape = RoundedCornerShape(2.dp),
                        ),
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = DebtshareTheme.typography.bodySmall,
            color = barColor,
        )
    }
}

@Composable
private fun TermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val primaryColor = DebtshareColors.Brand.primary
    val textColor = DebtshareTheme.colors.textSecondary

    val termsText = buildAnnotatedString {
        withStyle(SpanStyle(color = textColor)) {
            append(stringResource(Res.string.terms_accept_prefix))
        }
        withLink(
            LinkAnnotation.Clickable(
                tag = "terms",
                styles = TextLinkStyles(SpanStyle(color = primaryColor, fontWeight = FontWeight.SemiBold)),
                linkInteractionListener = { onTermsClick() },
            ),
        ) {
            append(stringResource(Res.string.terms_link))
        }
        withStyle(SpanStyle(color = textColor)) {
            append(stringResource(Res.string.terms_and_conjunction))
        }
        withLink(
            LinkAnnotation.Clickable(
                tag = "privacy",
                styles = TextLinkStyles(SpanStyle(color = primaryColor, fontWeight = FontWeight.SemiBold)),
                linkInteractionListener = { onPrivacyPolicyClick() },
            ),
        ) {
            append(stringResource(Res.string.privacy_policy_link))
        }
    }

    Row(
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { onCheckedChange(!checked) },
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                checkedColor = DebtshareColors.Brand.primary,
            ),
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = termsText,
            style = DebtshareTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun AlreadyHaveAccount(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.already_have_account) + " ",
            style = DebtshareTheme.typography.bodyMedium,
            color = DebtshareTheme.colors.textTertiary,
        )
        Text(
            text = stringResource(Res.string.sign_in),
            style = DebtshareTheme.typography.bodyMedium,
            color = DebtshareColors.Brand.primary,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onLoginClick,
            ),
        )
    }
}

private enum class PasswordStrength(
    val barCount: Int,
    val color: Color,
    val labelRes: org.jetbrains.compose.resources.StringResource,
) {
    None(0, Color.Transparent, Res.string.password_strength_weak),
    Weak(1, DebtshareColors.Semantic.error, Res.string.password_strength_weak),
    Fair(2, DebtshareColors.Accent.mustard, Res.string.password_strength_fair),
    Strong(3, DebtshareColors.Semantic.success, Res.string.password_strength_strong),
    VeryStrong(4, DebtshareColors.Semantic.success, Res.string.password_strength_very_strong),
}

private fun computePasswordStrength(password: String): PasswordStrength {
    if (password.isEmpty()) return PasswordStrength.None
    var score = 0
    if (password.length >= 10) score++
    if (password.any { it.isUpperCase() }) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++
    return when (score) {
        0, 1 -> PasswordStrength.Weak
        2 -> PasswordStrength.Fair
        3 -> PasswordStrength.Strong
        else -> PasswordStrength.VeryStrong
    }
}

private class SignUpUiStateProvider : PreviewParameterProvider<SignUpUiState> {
    override val values = sequenceOf(
        SignUpUiState.Idle,
        SignUpUiState.Loading,
        SignUpUiState.Error("Something went wrong. Please try again."),
    )
}

@DebtshareScreenPreview
@Composable
private fun SignUpScreenPreview(
    @PreviewParameter(SignUpUiStateProvider::class) uiState: SignUpUiState,
) {
    DebtshareTheme(darkTheme = false) {
        SignUpContent(
            isLoading = uiState is SignUpUiState.Loading,
            errorMessage = (uiState as? SignUpUiState.Error)?.message,
            isAliasAvailable = true,
            isAliasTaken = false,
            aliasSuggestions = emptyList<String>().toPersistentList(),
            onSignUpClick = { _, _, _, _ -> },
            onAliasChange = {},
            onLoginClick = {},
            onTermsClick = {},
            onPrivacyPolicyClick = {},
            onBackClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun SignUpScreenDarkPreview(
    @PreviewParameter(SignUpUiStateProvider::class) uiState: SignUpUiState,
) {
    DebtshareTheme(darkTheme = true) {
        SignUpContent(
            isLoading = uiState is SignUpUiState.Loading,
            errorMessage = (uiState as? SignUpUiState.Error)?.message,
            isAliasAvailable = false,
            isAliasTaken = true,
            aliasSuggestions = listOf("anaG_28", "ana.gomez").toPersistentList(),
            onSignUpClick = { _, _, _, _ -> },
            onAliasChange = {},
            onLoginClick = {},
            onTermsClick = {},
            onPrivacyPolicyClick = {},
            onBackClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun SignUpScreenAliasTakenPreview() {
    DebtshareTheme(darkTheme = false) {
        SignUpContent(
            isLoading = false,
            errorMessage = null,
            isAliasAvailable = false,
            isAliasTaken = true,
            aliasSuggestions = listOf("anaG_28", "ana.gomez").toPersistentList(),
            onSignUpClick = { _, _, _, _ -> },
            onAliasChange = {},
            onLoginClick = {},
            onTermsClick = {},
            onPrivacyPolicyClick = {},
            onBackClick = {},
        )
    }
}
