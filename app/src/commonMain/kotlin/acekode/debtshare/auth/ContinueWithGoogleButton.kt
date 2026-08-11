package acekode.debtshare.auth

import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.google_logo
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun ContinueWithGoogleButton(
    onResult: (GoogleSignInResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    val client = rememberGoogleSignInClient()
    val scope = rememberCoroutineScope()
    val colors = DebtshareTheme.colors

    OutlinedButton(
        onClick = {
            scope.launch {
                onResult(client.signIn())
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = colors.googleButtonBackground,
            contentColor = colors.googleButtonContent,
        ),
        border = BorderStroke(1.dp, colors.googleButtonBorder),
    ) {
        Icon(
            painter = painterResource(Res.drawable.google_logo),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Continue with Google",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
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
