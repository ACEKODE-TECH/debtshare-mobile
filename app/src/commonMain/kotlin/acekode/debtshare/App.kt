package acekode.debtshare

import acekode.debtshare.di.AppModule
import acekode.debtshare.presentation.login.LoginScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.ksp.generated.module

@Composable
fun App() {
    KoinApplication(
        application = {
            modules(AppModule().module)
        },
    ) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                LoginScreen()
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
