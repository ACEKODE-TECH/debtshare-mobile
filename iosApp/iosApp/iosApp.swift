import SwiftUI
import FirebaseCore
import shared

@main
struct iOSApp: App {
    init() {
        FirebaseApp.configure()
        GoogleSignInBridgeRegistry.shared.bridge = IosGoogleSignInBridge()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}