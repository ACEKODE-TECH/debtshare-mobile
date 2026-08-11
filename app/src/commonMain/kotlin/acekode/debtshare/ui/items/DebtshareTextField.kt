package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import acekode.debtshare.ui.utils.PreviewGallery
import acekode.debtshare.ui.utils.PreviewLabel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.close
import debtshare.app.generated.resources.eye
import debtshare.app.generated.resources.eye_off
import debtshare.app.generated.resources.search
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class DebtshareTextFieldVariant { Text, TextArea, Numeric, Search, Password }

object DebtshareTextFieldDefaults {
    val minHeight: Dp = 32.dp
    val horizontalPadding: Dp = 16.dp
    val verticalPadding: Dp = 16.dp
    val iconSize: Dp = 16.dp
    val borderWidth: Dp = 1.dp
    val emphasisBorderWidth: Dp = 2.dp
    const val TEXT_AREA_MIN_LINES = 3
    const val TEXT_AREA_MAX_LINES = 6
}

@Composable
fun DebtshareTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    variant: DebtshareTextFieldVariant = DebtshareTextFieldVariant.Text,
    label: String? = null,
    placeholder: String? = null,
    helpText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    currencySymbol: String? = null,
) {
    val textTertiary = DebtshareTheme.colors.textTertiary

    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label.uppercase(),
                style = DebtshareTheme.typography.labelUppercase,
                color = textTertiary,
            )
            Spacer(Modifier.height(DebtshareTheme.spacing.verySmall))
        }
        DebtshareTextFieldBox(
            value = value,
            onValueChange = onValueChange,
            variant = variant,
            placeholder = placeholder,
            enabled = enabled,
            readOnly = readOnly,
            currencySymbol = currencySymbol,
            isError = errorText != null,
        )
        val footer = errorText ?: helpText
        if (footer != null) {
            Spacer(Modifier.height(DebtshareTheme.spacing.verySmall))
            Text(
                text = footer,
                style = DebtshareTheme.typography.bodySmall.copy(
                    fontWeight = if (errorText != null) FontWeight.SemiBold else FontWeight.Medium,
                ),
                color = if (errorText != null) DebtshareColors.Semantic.error else textTertiary,
            )
        }
    }
}

@Composable
private fun DebtshareTextFieldBox(
    value: String,
    onValueChange: (String) -> Unit,
    variant: DebtshareTextFieldVariant,
    placeholder: String?,
    enabled: Boolean,
    readOnly: Boolean,
    currencySymbol: String?,
    isError: Boolean,
) {
    val textTertiary = DebtshareTheme.colors.textTertiary
    val textMuted = DebtshareTheme.colors.textMuted
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    val focused by interactionSource.collectIsFocusedAsState()
    val shape = RoundedCornerShape(DebtshareTheme.radius.medium)
    val isNumeric = variant == DebtshareTextFieldVariant.Numeric
    val isTextArea = variant == DebtshareTextFieldVariant.TextArea
    val isPassword = variant == DebtshareTextFieldVariant.Password
    var passwordVisible by remember { mutableStateOf(false) }

    Row(
        modifier = fieldContainerModifier(
            shape = shape,
            enabled = enabled,
            readOnly = readOnly,
            focused = focused,
            isError = isError,
        ),
        horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.small),
        verticalAlignment = if (isTextArea) Alignment.Top else Alignment.CenterVertically,
    ) {
        if (variant == DebtshareTextFieldVariant.Search) {
            FieldIcon(icon = Res.drawable.search, tint = textMuted)
        }
        if (isNumeric && currencySymbol != null) {
            Text(
                text = currencySymbol,
                style = DebtshareTheme.typography.bodyMedium,
                color = textTertiary,
            )
        }
        DebtshareTextFieldInput(
            value = value,
            onValueChange = onValueChange,
            variant = variant,
            placeholder = placeholder,
            enabled = enabled,
            readOnly = readOnly,
            interactionSource = interactionSource,
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
        )
        if (variant == DebtshareTextFieldVariant.Search && value.isNotEmpty() && enabled) {
            FieldIcon(
                icon = Res.drawable.close,
                tint = textTertiary,
                onClick = {
                    onValueChange("")
                    focusRequester.requestFocus()
                },
            )
        }
        if (isPassword) {
            FieldIcon(
                icon = if (passwordVisible) Res.drawable.eye else Res.drawable.eye_off,
                tint = textMuted,
                onClick = if (enabled) ({ passwordVisible = !passwordVisible }) else null,
            )
        }
    }
}

@Composable
private fun FieldIcon(
    icon: DrawableResource,
    tint: Color,
    onClick: (() -> Unit)? = null,
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = null,
        tint = tint,
        modifier = Modifier
            .size(DebtshareTextFieldDefaults.iconSize)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    )
}

@Composable
private fun DebtshareTextFieldInput(
    value: String,
    onValueChange: (String) -> Unit,
    variant: DebtshareTextFieldVariant,
    placeholder: String?,
    enabled: Boolean,
    readOnly: Boolean,
    interactionSource: MutableInteractionSource,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier,
) {
    val textPrimary = DebtshareTheme.colors.textPrimary
    val textSecondary = DebtshareTheme.colors.textSecondary
    val textMuted = DebtshareTheme.colors.textMuted
    val isNumeric = variant == DebtshareTextFieldVariant.Numeric
    val isTextArea = variant == DebtshareTextFieldVariant.TextArea
    val isPassword = variant == DebtshareTextFieldVariant.Password

    BasicTextField(
        value = value,
        onValueChange = { if (isNumeric) onValueChange(sanitizeNumeric(it)) else onValueChange(it) },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = DebtshareTheme.typography.bodyMedium.copy(
            color = if (readOnly) textSecondary else textPrimary,
            textAlign = if (isNumeric) TextAlign.End else TextAlign.Start,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = when {
                isNumeric -> KeyboardType.Decimal
                isPassword -> KeyboardType.Password
                else -> KeyboardType.Text
            },
        ),
        visualTransformation = visualTransformation,
        singleLine = !isTextArea,
        minLines = if (isTextArea) DebtshareTextFieldDefaults.TEXT_AREA_MIN_LINES else 1,
        maxLines = if (isTextArea) DebtshareTextFieldDefaults.TEXT_AREA_MAX_LINES else 1,
        cursorBrush = SolidColor(DebtshareColors.Brand.primary),
        interactionSource = interactionSource,
        decorationBox = { inner ->
            Box(contentAlignment = if (isNumeric) Alignment.CenterEnd else Alignment.CenterStart) {
                if (value.isEmpty() && placeholder != null) {
                    Text(
                        text = placeholder,
                        style = DebtshareTheme.typography.bodyMedium,
                        color = textMuted,
                    )
                }
                inner()
            }
        },
    )
}

@Composable
private fun fieldContainerModifier(
    shape: Shape,
    enabled: Boolean,
    readOnly: Boolean,
    focused: Boolean,
    isError: Boolean,
): Modifier {
    val fieldBackground = DebtshareTheme.colors.card
    val fieldDisabledBackground = DebtshareTheme.colors.neutralTint
    val fieldReadOnlyBackground = DebtshareTheme.colors.background
    val fieldReadOnlyBorder = DebtshareTheme.colors.neutralTint
    val controlBorder = DebtshareTheme.colors.border

    return Modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = DebtshareTextFieldDefaults.minHeight)
        .background(
            color = when {
                !enabled -> fieldDisabledBackground
                readOnly -> fieldReadOnlyBackground
                else -> fieldBackground
            },
            shape = shape,
        )
        .border(
            width = if (focused || isError) {
                DebtshareTextFieldDefaults.emphasisBorderWidth
            } else {
                DebtshareTextFieldDefaults.borderWidth
            },
            color = when {
                isError -> DebtshareColors.Semantic.error
                focused -> DebtshareColors.Brand.primary
                !enabled -> controlBorder
                readOnly -> fieldReadOnlyBorder
                else -> controlBorder
            },
            shape = shape,
        )
        .padding(
            horizontal = DebtshareTextFieldDefaults.horizontalPadding,
            vertical = DebtshareTextFieldDefaults.verticalPadding,
        )
}

private fun sanitizeNumeric(input: String): String {
    var separatorSeen = false
    return buildString {
        input.forEach { char ->
            when {
                char.isDigit() -> append(char)

                (char == '.' || char == ',') && !separatorSeen -> {
                    separatorSeen = true
                    append(char)
                }
            }
        }
    }
}

@Composable
private fun TextFieldVariantsGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Text")
        DebtshareTextField(
            value = "Cena en Casa Paco",
            onValueChange = {},
            label = "Concepto",
            placeholder = "Añade un concepto",
        )
        PreviewLabel("Numeric")
        DebtshareTextField(
            value = "1284,50",
            onValueChange = {},
            variant = DebtshareTextFieldVariant.Numeric,
            label = "Importe",
            currencySymbol = "€",
        )
        PreviewLabel("Search")
        DebtshareTextField(
            value = "playa",
            onValueChange = {},
            variant = DebtshareTextFieldVariant.Search,
            placeholder = "Buscar gastos",
        )
        PreviewLabel("Text area")
        DebtshareTextField(
            value = "Notas del gasto compartido entre los cuatro del piso.",
            onValueChange = {},
            variant = DebtshareTextFieldVariant.TextArea,
            label = "Notas",
            helpText = "Máximo 6 líneas visibles",
        )
        PreviewLabel("Password")
        DebtshareTextField(
            value = "miContraseña123",
            onValueChange = {},
            variant = DebtshareTextFieldVariant.Password,
            label = "Contraseña",
            placeholder = "Introduce tu contraseña",
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareTextFieldVariantsPreview() {
    TextFieldVariantsGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareTextFieldVariantsDarkPreview() {
    TextFieldVariantsGallery(darkTheme = true)
}

@Composable
private fun TextFieldStatesGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Empty")
        DebtshareTextField(value = "", onValueChange = {}, placeholder = "Añade un concepto")
        PreviewLabel("Filled + help")
        DebtshareTextField(
            value = "Cena en Casa Paco",
            onValueChange = {},
            helpText = "Se repartirá entre 4 personas",
        )
        PreviewLabel("Error")
        DebtshareTextField(
            value = "0",
            onValueChange = {},
            variant = DebtshareTextFieldVariant.Numeric,
            currencySymbol = "€",
            errorText = "El importe debe ser mayor que cero",
        )
        PreviewLabel("Disabled")
        DebtshareTextField(value = "Cena en Casa Paco", onValueChange = {}, enabled = false)
        PreviewLabel("Read only")
        DebtshareTextField(value = "Grupo Islandia 2026", onValueChange = {}, readOnly = true)
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareTextFieldStatesPreview() {
    TextFieldStatesGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareTextFieldStatesDarkPreview() {
    TextFieldStatesGallery(darkTheme = true)
}
