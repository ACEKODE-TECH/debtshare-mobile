package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DebtshareTabHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    endContent: @Composable () -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = DebtshareTheme.typography.displayMedium,
                color = DebtshareTheme.colors.textPrimary,
                modifier = Modifier.weight(1f),
            )

            endContent()
        }

        if (subtitle != null) {
            Spacer(Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = DebtshareTheme.typography.bodyMedium,
                color = DebtshareTheme.colors.textSecondary,
            )
        }
    }
}
