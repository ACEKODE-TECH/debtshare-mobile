package acekode.debtshare.utils

import acekode.debtshare.ui.theme.DebtshareTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.share
import org.jetbrains.compose.resources.painterResource
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIWindowScene

actual fun shareText(text: String, title: String) {
    val activityController = UIActivityViewController(
        activityItems = listOf(text),
        applicationActivities = null,
    )
    val windowScene = UIApplication.sharedApplication.connectedScenes
        .firstOrNull { it is UIWindowScene } as? UIWindowScene
    val rootViewController = windowScene?.windows?.firstOrNull()?.rootViewController
    rootViewController?.presentViewController(activityController, animated = true, completion = null)
}

@Composable
actual fun NativeShareTargets(text: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { shareText(text) }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(Res.drawable.share),
            contentDescription = null,
            tint = DebtshareTheme.colors.textPrimary,
            modifier = Modifier.size(48.dp),
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Share",
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textSecondary,
        )
    }
}
