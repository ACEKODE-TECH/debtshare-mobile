package acekode.debtshare

import acekode.debtshare.di.AppModule
import acekode.debtshare.di.ktorModule
import acekode.debtshare.di.localSettingsModule
import acekode.debtshare.navigation.NavGraph
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.KoinApplication
import org.koin.ksp.generated.module

@Composable
fun App() {
    KoinApplication(
        application = {
            modules(AppModule().module, ktorModule, localSettingsModule)
        },
    ) {
        DebtshareTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = DebtshareTheme.colors.background,
            ) {
                NavGraph()
            }
        }
    }
}

@DebtshareScreenPreview
@Composable
private fun AppPreview() {
    App()
}
