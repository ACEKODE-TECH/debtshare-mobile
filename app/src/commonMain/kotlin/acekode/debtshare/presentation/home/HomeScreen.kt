package acekode.debtshare.presentation.home

import acekode.debtshare.navigation.Screen
import acekode.debtshare.presentation.home.activity.ActivityScreen
import acekode.debtshare.presentation.home.groups.GroupsScreen
import acekode.debtshare.presentation.home.profile.ProfileScreen
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route

    Scaffold(
        bottomBar = {
            HomeBottomBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.Groups.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.Groups.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(Screen.Home.Groups.route) { GroupsScreen() }
            composable(Screen.Home.Activity.route) { ActivityScreen() }
            composable(Screen.Home.Profile.route) { ProfileScreen() }
        }
    }
}

@Composable
private fun HomeBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = DebtshareTheme.colors.card,
        contentColor = DebtshareTheme.colors.textTertiary,
        tonalElevation = 0.dp,
    ) {
        HomeTab.entries.forEach { tab ->
            val selected = currentRoute == tab.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(tab.route) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = stringResource(tab.label),
                    )
                },
                label = { Text(text = stringResource(tab.label)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DebtshareColors.Brand.primary,
                    selectedTextColor = DebtshareColors.Brand.primary,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = DebtshareTheme.colors.textTertiary,
                    unselectedTextColor = DebtshareTheme.colors.textTertiary,
                ),
            )
        }
    }
}

@DebtshareComponentPreview
@Composable
private fun HomeBottomBarGroupsSelectedPreview() {
    DebtshareTheme(darkTheme = false) {
        HomeBottomBar(
            currentRoute = Screen.Home.Groups.route,
            onNavigate = {},
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun HomeBottomBarActivitySelectedPreview() {
    DebtshareTheme(darkTheme = false) {
        HomeBottomBar(
            currentRoute = Screen.Home.Activity.route,
            onNavigate = {},
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun HomeBottomBarDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        HomeBottomBar(
            currentRoute = Screen.Home.Groups.route,
            onNavigate = {},
        )
    }
}
