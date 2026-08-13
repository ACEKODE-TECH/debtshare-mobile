package acekode.debtshare.auth

import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.google_logo
import debtshare.app.generated.resources.sign_in_with_google
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ContinueWithGoogleButton(
    onResult: (GoogleSignInResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    val client = rememberGoogleSignInClient()
    val scope = rememberCoroutineScope()
    val colors = DebtshareTheme.colors
    val shape = RoundedCornerShape(DebtshareTheme.radius.medium)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(shape)
            .background(colors.googleButtonBackground)
            .border(1.dp, colors.googleButtonBorder, shape)
            .clickable {
                scope.launch {
                    onResult(client.signIn())
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.google_logo),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified,
            )
            Text(
                text = stringResource(Res.string.sign_in_with_google),
                style = DebtshareTheme.typography.bodyLarge,
                color = colors.googleButtonContent,
            )
        }
    }
}

@DebtshareComponentPreview
@Composable
private fun ContinueWithGoogleButtonLightPreview() {
    DebtshareTheme(darkTheme = false) {
        Surface(
            color = DebtshareTheme.colors.background,
            modifier = Modifier.padding(16.dp),
        ) {
            ContinueWithGoogleButton(onResult = {})
        }
    }
}

@DebtshareComponentPreview
@Composable
private fun ContinueWithGoogleButtonDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        Surface(
            color = DebtshareTheme.colors.background,
            modifier = Modifier.padding(16.dp),
        ) {
            ContinueWithGoogleButton(onResult = {})
        }
    }
}
