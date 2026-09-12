package acekode.debtshare.googleAuth

import acekode.debtshare.utils.logDebug
import acekode.debtshare.utils.logError
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private const val TAG = "GoogleSignIn"

actual class GoogleSignInClient {
    actual suspend fun signIn(): GoogleSignInResult {
        val bridge = GoogleSignInBridgeRegistry.bridge
            ?: run {
                logError(TAG, "Bridge not registered — call GoogleSignInBridgeRegistry.bridge = ... at app startup")
                return GoogleSignInResult.Error("Google sign-in is not configured on iOS yet")
            }

        return suspendCancellableCoroutine { continuation ->
            bridge.signIn { result ->
                logDebug(TAG, "Sign-in result: $result")
                continuation.resume(result)
            }
        }
    }
}

@Composable
actual fun rememberGoogleSignInClient(): GoogleSignInClient = remember { GoogleSignInClient() }
