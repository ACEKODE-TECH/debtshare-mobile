package acekode.debtshare.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.google_logo
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

private val GoogleButtonLightBackground = Color(0xFFFFFFFF)
private val GoogleButtonLightBorder = Color(0xFF747775)
private val GoogleButtonLightContent = Color(0xFF1F1F1F)
private val GoogleButtonDarkBackground = Color(0xFF131314)
private val GoogleButtonDarkContent = Color(0xFFE3E3E3)

@Composable
fun ContinueWithGoogleButton(
    onResult: (GoogleSignInResult) -> Unit,
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),
) {
    val client = rememberGoogleSignInClient()
    val scope = rememberCoroutineScope()
    val backgroundColor = if (darkTheme) GoogleButtonDarkBackground else GoogleButtonLightBackground
    val contentColor = if (darkTheme) GoogleButtonDarkContent else GoogleButtonLightContent

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
            containerColor = backgroundColor,
            contentColor = contentColor,
        ),
        border = if (darkTheme) null else BorderStroke(1.dp, GoogleButtonLightBorder),
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

@Preview
@Composable
private fun ContinueWithGoogleButtonLightPreview() {
    MaterialTheme {
        Surface(color = Color.White) {
            ContinueWithGoogleButton(
                onResult = {},
                modifier = Modifier.padding(16.dp),
                darkTheme = false,
            )
        }
    }
}

@Preview
@Composable
private fun ContinueWithGoogleButtonDarkPreview() {
    MaterialTheme {
        Surface(color = Color.Black) {
            ContinueWithGoogleButton(
                onResult = {},
                modifier = Modifier.padding(16.dp),
                darkTheme = true,
            )
        }
    }
}
