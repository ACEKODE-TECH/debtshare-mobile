package acekode.debtshare.ui.utils

import acekode.debtshare.ui.theme.DebtshareTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun PreviewGallery(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = false,
    fillMaxSize: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    DebtshareTheme(darkTheme = darkTheme) {
        Column(
            modifier = modifier
                .then(if (fillMaxSize) Modifier.fillMaxSize() else Modifier)
                .background(DebtshareTheme.colors.background)
                .verticalScroll(rememberScrollState())
                .padding(DebtshareTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.medium),
            content = content,
        )
    }
}

@Composable
internal fun PreviewLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    val textTertiary = DebtshareTheme.colors.textTertiary
    Text(
        text = text,
        style = DebtshareTheme.typography.labelUppercase,
        color = textTertiary,
        modifier = modifier.fillMaxWidth(),
    )
}
