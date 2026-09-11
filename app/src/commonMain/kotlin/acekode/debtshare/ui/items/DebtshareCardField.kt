package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.file
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun DebtshareCardField(
    value: String,
    onValueChange: (String) -> Unit,
    icon: DrawableResource,
    label: String,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val fieldStyle = DebtshareTheme.typography.bodyLarge.copy(
        fontWeight = FontWeight.SemiBold,
        color = DebtshareTheme.colors.textPrimary,
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DebtshareTheme.colors.card)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { focusRequester.requestFocus() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DebtshareIcon(
            icon = icon,
            tint = DebtshareTheme.colors.textTertiary,
            size = DebtshareIconSize.Medium,
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textTertiary,
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = fieldStyle,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                cursorBrush = SolidColor(DebtshareColors.Brand.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = "—",
                                style = fieldStyle,
                                color = DebtshareTheme.colors.textTertiary,
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }
        if (trailing != null) trailing()
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareCardFieldPreview() {
    DebtshareTheme(darkTheme = false) {
        DebtshareCardField(
            value = "Compra Mercadona",
            onValueChange = {},
            icon = Res.drawable.file,
            label = "Descripción",
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareCardFieldEmptyPreview() {
    DebtshareTheme(darkTheme = false) {
        DebtshareCardField(
            value = "",
            onValueChange = {},
            icon = Res.drawable.file,
            label = "Descripción",
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareCardFieldDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        DebtshareCardField(
            value = "Compra Mercadona",
            onValueChange = {},
            icon = Res.drawable.file,
            label = "Descripción",
        )
    }
}
