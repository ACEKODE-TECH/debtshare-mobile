package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import acekode.debtshare.ui.utils.PreviewGallery
import acekode.debtshare.ui.utils.PreviewLabel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun DebtshareAvatarGroup(
    avatars: ImmutableList<DebtshareAvatarItem>,
    modifier: Modifier = Modifier,
    size: DebtshareAvatarSize = DebtshareAvatarSize.Medium,
    maxVisible: Int = 3,
    surfaceColor: Color = DebtshareTheme.colors.card,
) {
    val circle = size.avatarCircle()
    val ordered = avatars.sortedByDescending { it.isCurrentUser }
    val visible = ordered.take(maxVisible)
    val overflow = ordered.size - visible.size

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(-circle.diameter * DebtshareAvatarDefaults.OVERLAP_RATIO),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        visible.forEach { item ->
            DebtshareAvatar(
                name = item.name,
                imageId = item.image,
                size = size,
                state = if (item.isCurrentUser) {
                    DebtshareAvatarState.CurrentUser
                } else {
                    DebtshareAvatarState.Default
                },
                cutOutColor = surfaceColor,
            )
        }
        if (overflow > 0) {
            Box(
                modifier = Modifier
                    .size(circle.diameter)
                    .border(DebtshareAvatarDefaults.ring, surfaceColor, CircleShape)
                    .clip(CircleShape)
                    .background(DebtshareTheme.colors.neutralTintStrong),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "+$overflow",
                    style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = DebtshareTheme.colors.textSecondary,
                )
            }
        }
    }
}

private val avatarGroupPreviewNames = listOf("Jorge Sanzo", "María López", "Ana Ruiz", "Carlos Mena", "Lucía Prat")

@Composable
private fun AvatarGroupGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Group · 3 visibles")
        DebtshareAvatarGroup(
            avatars = avatarGroupPreviewNames.map {
                DebtshareAvatarItem(name = it, isCurrentUser = it == avatarGroupPreviewNames[3])
            }.toPersistentList(),
        )
        PreviewLabel("Group · sin overflow")
        DebtshareAvatarGroup(
            avatars = avatarGroupPreviewNames.take(2).map { DebtshareAvatarItem(name = it) }
                .toPersistentList(),
            size = DebtshareAvatarSize.Large,
        )
        PreviewLabel("Group · small")
        DebtshareAvatarGroup(
            avatars = avatarGroupPreviewNames.map { DebtshareAvatarItem(name = it) }
                .toPersistentList(),
            size = DebtshareAvatarSize.Small,
            maxVisible = 4,
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareAvatarGroupPreview() {
    AvatarGroupGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareAvatarGroupDarkPreview() {
    AvatarGroupGallery(darkTheme = true)
}
