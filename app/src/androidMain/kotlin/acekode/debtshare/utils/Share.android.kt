package acekode.debtshare.utils

import acekode.debtshare.AppContext
import acekode.debtshare.ui.theme.DebtshareTheme
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

actual fun shareText(text: String, title: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooser = Intent.createChooser(intent, title).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    AppContext.context.startActivity(chooser)
}

@Immutable
private data class ShareTarget(
    val label: String,
    val icon: ImageBitmap,
    val packageName: String,
    val activityName: String,
)

@Composable
actual fun NativeShareTargets(text: String, modifier: Modifier) {
    val context = LocalContext.current
    val targets = remember {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply { type = "text/plain" }
            context.packageManager.queryIntentActivities(intent, 0)
                .distinctBy { it.activityInfo.packageName }
                .map { resolveInfo ->
                    val pm = context.packageManager
                    ShareTarget(
                        label = resolveInfo.loadLabel(pm).toString(),
                        icon = resolveInfo.loadIcon(pm).toImageBitmap(),
                        packageName = resolveInfo.activityInfo.packageName,
                        activityName = resolveInfo.activityInfo.name,
                    )
                }
        } catch (_: Exception) {
            emptyList()
        }
    }

    if (targets.isEmpty()) {
        ShareFallbackButton(text = text)
        return
    }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(targets) { target ->
            ShareTargetItem(
                target = target,
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, text)
                        component = ComponentName(target.packageName, target.activityName)
                    }
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(shareIntent)
                },
            )
        }
    }
}

@Composable
private fun ShareTargetItem(
    target: ShareTarget,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            bitmap = target.icon,
            contentDescription = target.label,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = target.label,
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ShareFallbackButton(text: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { shareText(text) }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Share",
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textSecondary,
        )
    }
}

private fun Drawable.toImageBitmap(): ImageBitmap {
    val bitmap = Bitmap.createBitmap(
        intrinsicWidth.coerceAtLeast(1),
        intrinsicHeight.coerceAtLeast(1),
        Bitmap.Config.ARGB_8888,
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap.asImageBitmap()
}
