package acekode.debtshare

import acekode.debtshare.navigation.NotificationRouter
import acekode.debtshare.ui.theme.DebtshareTheme
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppContext.context = applicationContext
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleNotificationIntent(intent)
        addOnNewIntentListener(::handleNotificationIntent)
        logFcmToken()
        setContent {
            DebtshareTheme {
                App()
            }
        }
    }

    private fun handleNotificationIntent(intent: Intent) {
        DebtshareMessagingService.extractNotification(intent)?.let { notification ->
            NotificationRouter.post(notification)
        }
    }

    private fun logFcmToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("FCM", "Token: $token")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DebtshareTheme {
        App()
    }
}
