package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import acekode.debtshare.ui.utils.PreviewGallery
import acekode.debtshare.ui.utils.PreviewLabel
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.google_logo
import debtshare.app.generated.resources.person
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class DebtshareAvatarSize { Small, Medium, Large }

enum class DebtshareAvatarState { Default, CurrentUser, Selected, Inactive, Loading }

@Immutable
data class DebtshareAvatarItem(
    val name: String,
    val image: DrawableResource? = null,
    val isCurrentUser: Boolean = false,
)

object DebtshareAvatarDefaults {
    val diameterSmall: Dp = 16.dp
    val diameterMedium: Dp = 32.dp
    val diameterLarge: Dp = 48.dp
    val ring: Dp = 2.dp

    const val OVERLAP_RATIO = 0.5f
    const val INACTIVE_ALPHA = 0.5f
    const val HALO_ALPHA = 0.5f
    const val LUMINANCE_THRESHOLD = 0.5f
    const val HASH_MULTIPLIER = 31
    const val HASH_MASK = 0x7FFFFFFF
    const val SHIMMER_MILLIS = 900
}

@Composable
fun DebtshareAvatar(
    name: String,
    modifier: Modifier = Modifier,
    imageId: DrawableResource? = null,
    size: DebtshareAvatarSize = DebtshareAvatarSize.Medium,
    state: DebtshareAvatarState = DebtshareAvatarState.Default,
    showPlaceholder: Boolean = false,
    cutOutColor: Color = Color.Unspecified,
) {
    val size = size.avatarCircle()
    val tint = avatarTintFor(name)
    val inactive = state == DebtshareAvatarState.Inactive

    Box(
        modifier = modifier
            .size(size.diameter)
            .alpha(if (inactive) DebtshareAvatarDefaults.INACTIVE_ALPHA else 1f)
            .avatarCutOut(cutOutColor)
            .avatarRing(state, size.ringWidth)
            .clip(CircleShape)
            .background(if (showPlaceholder || imageId != null) DebtshareTheme.colors.neutralTint else tint),
        contentAlignment = Alignment.Center,
    ) {
        AvatarContent(
            name = name,
            imageId = imageId,
            size = size,
            tint = tint,
            state = state,
            showPlaceholder = showPlaceholder,
        )
    }
}

@Composable
private fun AvatarContent(
    name: String,
    imageId: DrawableResource?,
    size: AvatarCircle,
    tint: Color,
    state: DebtshareAvatarState,
    showPlaceholder: Boolean,
) {
    when {
        state == DebtshareAvatarState.Loading -> {
            val transition = rememberInfiniteTransition(label = "avatarSkeleton")
            val shimmer by transition.animateColor(
                initialValue = DebtshareTheme.colors.neutralTint,
                targetValue = DebtshareTheme.colors.skeletonHighlight,
                animationSpec = infiniteRepeatable(
                    animation = tween(DebtshareAvatarDefaults.SHIMMER_MILLIS),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "avatarSkeletonColor",
            )
            Box(Modifier.fillMaxSize().background(shimmer))
        }

        imageId != null -> Image(
            painter = painterResource(imageId),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            colorFilter = if (state == DebtshareAvatarState.Inactive) GrayscaleFilter else null,
            modifier = Modifier.fillMaxSize(),
        )

        showPlaceholder -> Icon(
            painter = painterResource(Res.drawable.person),
            contentDescription = null,
            tint = DebtshareTheme.colors.textMuted,
            modifier = Modifier.fillMaxSize().padding(size.glyphPadding),
        )

        else -> Text(
            text = initialsOf(name),
            style = size.textStyle,
            color = if (tint.luminance() > DebtshareAvatarDefaults.LUMINANCE_THRESHOLD) {
                DebtshareTheme.colors.textPrimary
            } else {
                DebtshareColors.Neutral.n0
            },
        )
    }
}

@Immutable
internal data class AvatarCircle(
    val diameter: Dp,
    val ringWidth: Dp,
    val glyphPadding: Dp,
    val textStyle: TextStyle,
)

private val GrayscaleFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })

private fun Modifier.avatarCutOut(color: Color): Modifier = if (color == Color.Unspecified) {
    this
} else {
    border(DebtshareAvatarDefaults.ring, color, CircleShape)
}

private fun Modifier.avatarRing(state: DebtshareAvatarState, ringWidth: Dp): Modifier = when (state) {
    DebtshareAvatarState.CurrentUser ->
        border(DebtshareAvatarDefaults.ring, DebtshareColors.Brand.primary, CircleShape)

    DebtshareAvatarState.Selected ->
        this
            .border(
                width = DebtshareAvatarDefaults.ring,
                color = DebtshareColors.Brand.primary.copy(alpha = DebtshareAvatarDefaults.HALO_ALPHA),
                shape = CircleShape,
            )
            .padding(DebtshareAvatarDefaults.ring)
            .border(ringWidth, DebtshareColors.Brand.primary, CircleShape)

    else -> this
}

@Composable
internal fun DebtshareAvatarSize.avatarCircle(): AvatarCircle = when (this) {
    DebtshareAvatarSize.Small -> AvatarCircle(
        diameter = DebtshareAvatarDefaults.diameterSmall,
        ringWidth = DebtshareAvatarDefaults.ring,
        glyphPadding = DebtshareTheme.spacing.verySmall,
        textStyle = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
    )

    DebtshareAvatarSize.Medium -> AvatarCircle(
        diameter = DebtshareAvatarDefaults.diameterMedium,
        ringWidth = DebtshareAvatarDefaults.ring,
        glyphPadding = DebtshareTheme.spacing.small,
        textStyle = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
    )

    DebtshareAvatarSize.Large -> AvatarCircle(
        diameter = DebtshareAvatarDefaults.diameterLarge,
        ringWidth = DebtshareAvatarDefaults.ring,
        glyphPadding = DebtshareTheme.spacing.large,
        textStyle = DebtshareTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold),
    )
}

internal fun initialsOf(name: String): String {
    val parts = name.trim().split(' ').filter { it.isNotBlank() }
    return when (parts.size) {
        0 -> ""
        1 -> parts[0].take(1).uppercase()
        else -> (parts[0].take(1) + parts[1].take(1)).uppercase()
    }
}

internal fun avatarTintFor(name: String): Color {
    val hash = name.fold(0) { acc, char ->
        (acc * DebtshareAvatarDefaults.HASH_MULTIPLIER + char.code) and DebtshareAvatarDefaults.HASH_MASK
    }
    return DebtshareColors.avatarTints[hash % DebtshareColors.avatarTints.size]
}

private val avatarPreviewNames = listOf("Jorge Sanzo", "María López", "Ana Ruiz", "Carlos Mena", "Lucía Prat")

@Composable
private fun AvatarSizesGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Initials · small medium large")
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareAvatarSize.entries.forEach { size ->
                DebtshareAvatar(name = avatarPreviewNames[0], size = size)
            }
        }
        PreviewLabel("Image")
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareAvatarSize.entries.forEach { size ->
                DebtshareAvatar(
                    name = avatarPreviewNames[1],
                    imageId = Res.drawable.google_logo,
                    size = size,
                )
            }
        }
        PreviewLabel("Placeholder")
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareAvatarSize.entries.forEach { size ->
                DebtshareAvatar(name = "", size = size, showPlaceholder = true)
            }
        }
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareAvatarSizesPreview() {
    AvatarSizesGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareAvatarSizesDarkPreview() {
    AvatarSizesGallery(darkTheme = true)
}

@Composable
private fun AvatarStatesGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Default · current user · selected · inactive · loading")
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareAvatarState.entries.forEach { state ->
                DebtshareAvatar(name = avatarPreviewNames[2], size = DebtshareAvatarSize.Large, state = state)
            }
        }
        PreviewLabel("Hash tints")
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            avatarPreviewNames.forEach { name ->
                DebtshareAvatar(name = name, size = DebtshareAvatarSize.Medium)
            }
        }
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareAvatarStatesPreview() {
    AvatarStatesGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareAvatarStatesDarkPreview() {
    AvatarStatesGallery(darkTheme = true)
}
