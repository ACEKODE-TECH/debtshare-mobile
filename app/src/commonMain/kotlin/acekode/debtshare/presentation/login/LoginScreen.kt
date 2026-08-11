package acekode.debtshare.presentation.login

import acekode.debtshare.auth.ContinueWithGoogleButton
import acekode.debtshare.auth.GoogleSignInResult
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginContent(
        platform = uiState.platform,
    )
}

@Composable
fun LoginContent(
    platform: String,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        var lastResult by remember { mutableStateOf<GoogleSignInResult?>(null) }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = platform,
                color = MaterialTheme.colorScheme.onBackground,
            )
            ContinueWithGoogleButton(
                onResult = { lastResult = it },
            )
            val status = when (val result = lastResult) {
                is GoogleSignInResult.Success -> "Signed in as ${result.account.email}"
                is GoogleSignInResult.Error -> "Error: ${result.message}"
                null -> ""
            }
            if (status.isNotEmpty()) {
                Text(text = status, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}

@DebtshareScreenPreview
@Composable
private fun LoginScreenPreview() {
    DebtshareTheme {
        LoginContent("Hello world!")
    }
}
