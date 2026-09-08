package acekode.debtshare.presentation.home.profile

import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.tab_profile
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = stringResource(Res.string.tab_profile))
    }
}

@DebtshareScreenPreview
@Composable
private fun ProfileScreenPreview() {
    DebtshareTheme(darkTheme = false) {
        ProfileScreen()
    }
}
