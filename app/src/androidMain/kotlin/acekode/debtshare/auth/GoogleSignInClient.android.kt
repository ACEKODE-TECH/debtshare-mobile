package acekode.debtshare.auth

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

private const val TAG = "GoogleSignIn"

actual class GoogleSignInClient(private val context: Context) {
    actual suspend fun signIn(): GoogleSignInResult {
        val option =
            GetGoogleIdOption.Builder()
                .setServerClientId(GoogleSignInConfig.WEB_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()

        return try {
            val credential = CredentialManager.create(context).getCredential(context, request).credential
            if (credential !is CustomCredential ||
                credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                return GoogleSignInResult.Error("Unexpected credential type: ${credential.type}")
            }

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val account =
                GoogleAccount(
                    id = googleIdTokenCredential.id,
                    displayName = googleIdTokenCredential.displayName,
                    email = googleIdTokenCredential.id,
                    photoUrl = googleIdTokenCredential.profilePictureUri?.toString(),
                )
            Log.i(TAG, "Signed in: ${account.email} (${account.displayName})")
            GoogleSignInResult.Success(account)
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Credential Manager sign-in failed", e)
            GoogleSignInResult.Error(e.message ?: "Google sign-in was cancelled or failed")
        } catch (e: GoogleIdTokenParsingException) {
            Log.e(TAG, "Failed to parse Google ID token", e)
            GoogleSignInResult.Error("Failed to parse Google credential")
        }
    }
}

@Composable
actual fun rememberGoogleSignInClient(): GoogleSignInClient {
    val context = LocalContext.current
    return remember { GoogleSignInClient(context) }
}
