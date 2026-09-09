package acekode.debtshare.presentation.home

import acekode.debtshare.navigation.Screen
import acekode.debtshare.presentation.home.activity.ActivityScreen
import acekode.debtshare.presentation.home.groups.GroupsScreen
import acekode.debtshare.presentation.home.profile.ProfileScreen
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route

    Scaffold(
        contentWindowInsets = WindowInsets(0),
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
            modifier = Modifier
                .padding(paddingValues)
                .windowInsetsPadding(WindowInsets.statusBars),
        ) {
            composable(Screen.Home.Groups.route) {
                GroupsScreen(
                    onCreateGroupClick = { },
                    onJoinWithCodeClick = { },
                    onSearchClick = { },
                    onGroupClick = { },
                )
            }
            composable(Screen.Home.Activity.route) { ActivityScreen() }
            composable(Screen.Home.Profile.route) {
                ProfileScreen(
                    onSettingsClick = { },
                    onLogoutClick = { },
                )
            }
        }
    }
}

@Composable
private fun HomeBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            thickness = 0.5.dp,
            color = DebtshareTheme.colors.textTertiary.copy(alpha = 0.3f),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DebtshareTheme.colors.card)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HomeTab.entries.forEach { tab ->
                val selected = currentRoute == tab.route
                BottomBarItem(
                    tab = tab,
                    selected = selected,
                    onClick = { onNavigate(tab.route) },
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    tab: HomeTab,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val color = if (selected) {
        DebtshareColors.Brand.primary
    } else {
        DebtshareTheme.colors.textTertiary
    }
    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(tab.icon),
            contentDescription = stringResource(tab.label),
            tint = color,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = stringResource(tab.label),
            color = color,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        )
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
