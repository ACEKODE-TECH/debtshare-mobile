package acekode.debtshare.auth

// GoogleSignIn-iOS is a Swift-only SPM package, unreachable from Kotlin/Native cinterop, so the
// real sign-in call is implemented in Swift and registered here at app launch.
interface GoogleSignInBridge {
    fun signIn(onResult: (GoogleSignInResult) -> Unit)
}

object GoogleSignInBridgeRegistry {
    var bridge: GoogleSignInBridge? = null
}
