package acekode.debtshare.navigation

import acekode.debtshare.googleAuth.GoogleAccount
import acekode.debtshare.presentation.googlealias.GoogleAliasScreen
import acekode.debtshare.presentation.home.HomeScreen
import acekode.debtshare.presentation.home.groups.joingroup.GroupTagsUiModel
import acekode.debtshare.presentation.home.groups.joingroup.JoinGroupScreen
import acekode.debtshare.presentation.home.groups.joingroup.JoinGroupUiState
import acekode.debtshare.presentation.login.LoginScreen
import acekode.debtshare.presentation.signup.SignUpScreen
import acekode.debtshare.presentation.splash.SplashScreen
import acekode.debtshare.utils.logDebug
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.collections.immutable.persistentListOf

@Suppress("LongMethod")
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    var googleAccount by remember { mutableStateOf<GoogleAccount?>(null) }
    val pendingNotification by NotificationRouter.pending.collectAsStateWithLifecycle()
    val initialNotification = remember { NotificationRouter.pending.value }
    val startDestination = remember {
        if (initialNotification != null) Screen.JoinGroup.route else Screen.Splash.route
    }

    LaunchedEffect(pendingNotification) {
        val current = navController.currentDestination?.route
        if (pendingNotification != null && current != Screen.JoinGroup.route) {
            navController.navigate(Screen.JoinGroup.route) {
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
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
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
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
            val account = googleAccount
            if (account != null) {
                GoogleAliasScreen(
                    account = account,
                    onNavigateBack = { navController.popBackStack() },
                    onContinueClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.popBackStack(Screen.Login.route, inclusive = false)
                }
            }
        }
        composable(route = Screen.Home.route) {
            HomeScreen()
        }
        composable(
            route = Screen.JoinGroup.route,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } },
        ) {
            JoinGroupDestination(pendingNotification) {
                NotificationRouter.consume()
                navigateToHome(navController)
            }
        }
    }
}

@Composable
private fun JoinGroupDestination(
    pendingNotification: PendingNotification?,
    onDismiss: () -> Unit,
) {
    val uiState = when (pendingNotification) {
        is PendingNotification.GroupInvite -> JoinGroupUiState.Valid(
            inviteCode = pendingNotification.inviteCode,
            groupName = pendingNotification.groupName,
            groupDescription = pendingNotification.groupDescription,
            inviterName = pendingNotification.inviterName,
            members = persistentListOf(),
            membersCount = pendingNotification.membersCount,
            tags = GroupTagsUiModel(
                currency = pendingNotification.stats.currency,
                expenseCount = pendingNotification.stats.expenseCount,
                isActive = pendingNotification.stats.isActive,
            ),
        )

        is PendingNotification.ExpiredGroupInvite -> JoinGroupUiState.Expired(
            inviteCode = pendingNotification.inviteCode,
            groupName = pendingNotification.groupName,
            generatedBy = pendingNotification.generatedBy,
            expiredAgo = pendingNotification.expiredAgo,
        )

        null -> return
    }
    JoinGroupScreen(
        uiState = uiState,
        onJoinClick = {
            logDebug("NavGraph", "Join group clicked")
            onDismiss()
        },
        onDeclineClick = {
            logDebug("NavGraph", "Decline group clicked")
            onDismiss()
        },
        onGoHomeClick = onDismiss,
    )
}

private fun navigateToHome(navController: androidx.navigation.NavController) {
    if (!navController.popBackStack(Screen.Home.route, inclusive = false)) {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.JoinGroup.route) { inclusive = true }
        }
    }
}
