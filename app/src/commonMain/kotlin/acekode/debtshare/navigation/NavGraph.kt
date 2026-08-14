package acekode.debtshare.navigation

import acekode.debtshare.auth.GoogleAccount
import acekode.debtshare.presentation.SplashScreen
import acekode.debtshare.presentation.googlealias.GoogleAliasScreen
import acekode.debtshare.presentation.login.LoginScreen
import acekode.debtshare.presentation.signup.SignUpScreen
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    var googleAccount by remember { mutableStateOf<GoogleAccount?>(null) }

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
        composable(
            route = Screen.Login.route,
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
        ) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Screen.Signup.route)
                },
                onNavigateToGoogleAlias = { account ->
                    googleAccount = account
                    navController.navigate(Screen.GoogleAlias.route)
                },
            )
        }
        composable(
            route = Screen.Signup.route,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } },
        ) {
            SignUpScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(
            route = Screen.GoogleAlias.route,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } },
        ) {
            googleAccount?.let { account ->
                GoogleAliasScreen(
                    account = account,
                    onNavigateBack = { navController.popBackStack() },
                    onContinueClick = { /* Navigate to home */ },
                )
            }
        }
    }
}
