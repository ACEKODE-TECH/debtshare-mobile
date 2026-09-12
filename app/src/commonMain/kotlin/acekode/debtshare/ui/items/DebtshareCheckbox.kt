package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import acekode.debtshare.ui.utils.PreviewGallery
import acekode.debtshare.ui.utils.PreviewLabel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DebtshareCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        colors = CheckboxDefaults.colors(
            checkedColor = DebtshareColors.Brand.primary,
        ),
    )
}

@Composable
private fun CheckboxGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Checked · Unchecked")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DebtshareCheckbox(checked = true, onCheckedChange = {})
            DebtshareCheckbox(checked = false, onCheckedChange = {})
        }
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareCheckboxPreview() {
    CheckboxGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareCheckboxDarkPreview() {
    CheckboxGallery(darkTheme = true)
}
