package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import acekode.debtshare.ui.utils.PreviewGallery
import acekode.debtshare.ui.utils.PreviewLabel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.bell
import debtshare.app.generated.resources.house
import debtshare.app.generated.resources.plus
import debtshare.app.generated.resources.search
import debtshare.app.generated.resources.settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun DebtshareIcon(
    icon: DrawableResource,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = DebtshareTheme.colors.textPrimary,
    size: DebtshareIconSize = DebtshareIconSize.Medium,
    onClick: (() -> Unit)? = null,
) {
    val baseModifier = modifier.size(size.iconSize)
    Icon(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        tint = tint,
        modifier = if (onClick != null) baseModifier.clickable(onClick = onClick) else baseModifier,
    )
}

@Composable
private fun IconSizesGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Small · Medium · Large")
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareIcon(icon = Res.drawable.search, size = DebtshareIconSize.Small)
            DebtshareIcon(icon = Res.drawable.search, size = DebtshareIconSize.Medium)
            DebtshareIcon(icon = Res.drawable.search, size = DebtshareIconSize.Large)
        }
        PreviewLabel("Clickable (header)")
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareIcon(icon = Res.drawable.search, onClick = {})
            DebtshareIcon(icon = Res.drawable.plus, onClick = {})
            DebtshareIcon(icon = Res.drawable.bell, onClick = {})
            DebtshareIcon(icon = Res.drawable.settings, onClick = {})
        }
        PreviewLabel("Decorative (no click)")
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareIcon(
                icon = Res.drawable.house,
                tint = DebtshareTheme.colors.textSecondary,
                size = DebtshareIconSize.Small,
            )
            DebtshareIcon(
                icon = Res.drawable.house,
                tint = DebtshareTheme.colors.textSecondary,
            )
        }
    }
}

enum class DebtshareIconSize(val iconSize: Dp) {
    Small(16.dp),
    Medium(24.dp),
    Large(32.dp),
}

@DebtshareComponentPreview
@Composable
private fun DebtshareIconPreview() {
    IconSizesGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareIconDarkPreview() {
    IconSizesGallery(darkTheme = true)
}
