package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import acekode.debtshare.ui.utils.PreviewGallery
import acekode.debtshare.ui.utils.PreviewLabel
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.plus
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class DebtshareButtonVariant { Primary, Secondary, Ghost, Destructive }

enum class DebtshareButtonSize { Small, Medium, Large }

object DebtshareButtonDefaults {
    val heightSmall: Dp = 16.dp
    val heightMedium: Dp = 24.dp
    val heightLarge: Dp = 32.dp
    val iconSmall: Dp = 12.dp
    val iconMedium: Dp = 16.dp
    val iconLarge: Dp = 20.dp
    val spinner: Dp = 16.dp
    val spinnerStroke: Dp = 2.dp
    val borderWidth: Dp = 1.dp
    val focusRingWidth: Dp = 2.dp
    const val TRANSITION_MILLIS = 150
    const val DISABLED_ALPHA_SECONDARY = 0.5f
    const val DISABLED_ALPHA_GHOST = 0.5f
    const val CONTENT_HIDDEN_ALPHA = 0f
    const val CONTENT_VISIBLE_ALPHA = 1f
}

@Composable
fun DebtshareButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: DebtshareButtonVariant = DebtshareButtonVariant.Primary,
    size: DebtshareButtonSize = DebtshareButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: DrawableResource? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val interactive = enabled && !loading
    val style = buttonStyle(variant, enabled)
    val buttonSize = size.buttonSize()
    val shape = RoundedCornerShape(DebtshareTheme.radius.medium)
    val container by animateColorAsState(
        targetValue = style.container,
        animationSpec = tween(DebtshareButtonDefaults.TRANSITION_MILLIS),
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = style.elevation,
                shape = shape,
                spotColor = style.shadow,
                ambientColor = style.shadow,
            )
            .clip(shape)
            .background(container)
            .buttonOutline(focused && enabled, style, shape)
            .clickable(
                enabled = interactive,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick,
            )
            .defaultMinSize(minHeight = buttonSize.minHeight)
            .padding(horizontal = buttonSize.horizontalPadding),
        contentAlignment = Alignment.Center,
    ) {
        ButtonContent(
            text = text,
            icon = icon,
            buttonSize = buttonSize,
            color = style.content,
            loading = loading,
        )
    }
}

@Composable
fun DebtshareIconButton(
    icon: DrawableResource,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: DebtshareButtonVariant = DebtshareButtonVariant.Secondary,
    size: DebtshareButtonSize = DebtshareButtonSize.Medium,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val style = buttonStyle(variant, enabled)
    val buttonSize = size.buttonSize()
    val shape = RoundedCornerShape(DebtshareTheme.radius.large)

    Box(
        modifier = modifier
            .size(buttonSize.minHeight)
            .shadow(style.elevation, shape, spotColor = style.shadow, ambientColor = style.shadow)
            .clip(shape)
            .background(style.container)
            .buttonOutline(focused && enabled, style, shape)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = style.content,
            modifier = Modifier.size(buttonSize.iconSize),
        )
    }
}

@Composable
private fun ButtonContent(
    text: String,
    icon: DrawableResource?,
    buttonSize: ButtonSize,
    color: Color,
    loading: Boolean,
) {
    Box(contentAlignment = Alignment.Center) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.alpha(
                if (loading) {
                    DebtshareButtonDefaults.CONTENT_HIDDEN_ALPHA
                } else {
                    DebtshareButtonDefaults.CONTENT_VISIBLE_ALPHA
                },
            ),
        ) {
            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(buttonSize.iconSize),
                )
            }
            Text(
                text = text,
                style = buttonSize.textStyle,
                color = color,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
            )
        }
        if (loading) {
            CircularProgressIndicator(
                color = color,
                strokeWidth = DebtshareButtonDefaults.spinnerStroke,
                modifier = Modifier.size(DebtshareButtonDefaults.spinner),
            )
        }
    }
}

@Composable
private fun ButtonVariantsGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Primary")
        DebtshareButton(text = "Añadir gasto", onClick = {})
        PreviewLabel("Secondary")
        DebtshareButton(
            text = "Ver detalles",
            onClick = {},
            variant = DebtshareButtonVariant.Secondary,
        )
        PreviewLabel("Ghost")
        DebtshareButton(
            text = "Cancelar",
            onClick = {},
            variant = DebtshareButtonVariant.Ghost,
        )
        PreviewLabel("Destructive")
        DebtshareButton(
            text = "Salir del grupo",
            onClick = {},
            variant = DebtshareButtonVariant.Destructive,
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareButtonVariantsPreview() {
    ButtonVariantsGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareButtonVariantsDarkPreview() {
    ButtonVariantsGallery(darkTheme = true)
}

@Composable
private fun ButtonSizesGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Small · Medium · Large")
        DebtshareButton(text = "Small", onClick = {}, size = DebtshareButtonSize.Small)
        DebtshareButton(text = "Medium", onClick = {}, size = DebtshareButtonSize.Medium)
        DebtshareButton(text = "Large", onClick = {}, size = DebtshareButtonSize.Large)
        PreviewLabel("Icon only")
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareIconButton(
                icon = Res.drawable.plus,
                contentDescription = "Añadir",
                onClick = {},
                size = DebtshareButtonSize.Small,
            )
            DebtshareIconButton(
                icon = Res.drawable.plus,
                contentDescription = "Añadir",
                onClick = {},
            )
            DebtshareIconButton(
                icon = Res.drawable.plus,
                contentDescription = "Añadir",
                onClick = {},
                variant = DebtshareButtonVariant.Primary,
                size = DebtshareButtonSize.Large,
            )
        }
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareButtonSizesPreview() {
    ButtonSizesGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareButtonSizesDarkPreview() {
    ButtonSizesGallery(darkTheme = true)
}

@Composable
private fun ButtonStatesGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Con icono")
        DebtshareButton(text = "Añadir gasto", onClick = {}, icon = Res.drawable.plus)
        PreviewLabel("Loading")
        DebtshareButton(text = "Añadir gasto", onClick = {}, loading = true)
        PreviewLabel("Disabled")
        DebtshareButton(text = "Primary", onClick = {}, enabled = false)
        DebtshareButton(
            text = "Secondary",
            onClick = {},
            variant = DebtshareButtonVariant.Secondary,
            enabled = false,
        )
        DebtshareButton(
            text = "Ghost",
            onClick = {},
            variant = DebtshareButtonVariant.Ghost,
            enabled = false,
        )
        DebtshareButton(
            text = "Destructive",
            onClick = {},
            variant = DebtshareButtonVariant.Destructive,
            enabled = false,
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareButtonStatesPreview() {
    ButtonStatesGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareButtonStatesDarkPreview() {
    ButtonStatesGallery(darkTheme = true)
}
