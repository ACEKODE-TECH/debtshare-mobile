package acekode.debtshare.navigation

import acekode.debtshare.presentation.SplashScreen
import acekode.debtshare.presentation.login.LoginScreen
import acekode.debtshare.presentation.signup.SignUpScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
            )
        }
        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Screen.Signup.route)
                },
            )
        }
        composable(route = Screen.Signup.route) {
            SignUpScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}
