package acekode.debtshare.auth

import androidx.compose.runtime.Composable

expect class GoogleSignInClient {
    suspend fun signIn(): GoogleSignInResult
}

@Composable
expect fun rememberGoogleSignInClient(): GoogleSignInClient
