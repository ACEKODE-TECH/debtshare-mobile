package acekode.debtshare.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class GoogleSignInClient {
    actual suspend fun signIn(): GoogleSignInResult {
        val bridge = GoogleSignInBridgeRegistry.bridge
            ?: return GoogleSignInResult.Error("Google sign-in is not configured on iOS yet")

        return suspendCancellableCoroutine { continuation ->
            bridge.signIn { result -> continuation.resume(result) }
        }
    }
}

@Composable
actual fun rememberGoogleSignInClient(): GoogleSignInClient = remember { GoogleSignInClient() }
