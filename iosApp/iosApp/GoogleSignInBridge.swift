import GoogleSignIn
import UIKit
import shared

// `GoogleSignInResult.Success`/`GoogleSignInResult.Error` are exported by Kotlin/Native as
// `GoogleSignInResultSuccess`/`GoogleSignInResultError` — verify these names against the
// generated `shared` module in Xcode once the framework has been built.
final class IosGoogleSignInBridge: GoogleSignInBridge {
    func signIn(onResult: @escaping (GoogleSignInResult) -> Void) {
        guard let rootViewController = UIApplication.shared.connectedScenes
            .compactMap({ $0 as? UIWindowScene })
            .flatMap({ $0.windows })
            .first(where: { $0.isKeyWindow })?.rootViewController
        else {
            onResult(GoogleSignInResultError(message: "No root view controller available"))
            return
        }

        GIDSignIn.sharedInstance.signIn(withPresenting: rootViewController) { signInResult, error in
            if let error {
                onResult(GoogleSignInResultError(message: error.localizedDescription))
                return
            }
            guard let user = signInResult?.user else {
                onResult(GoogleSignInResultError(message: "Missing Google user"))
                return
            }

            let account = GoogleAccount(
                id: user.userID ?? "",
                displayName: user.profile?.name,
                email: user.profile?.email,
                photoUrl: user.profile?.imageURL(withDimension: 320)?.absoluteString
            )
            print("GoogleSignIn success: \(account.email ?? "unknown")")
            onResult(GoogleSignInResultSuccess(account: account))
        }
    }
}
