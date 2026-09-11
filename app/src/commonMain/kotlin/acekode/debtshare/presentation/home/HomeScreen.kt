package acekode.debtshare.presentation.home

import acekode.debtshare.navigation.Screen
import acekode.debtshare.presentation.home.activity.ActivityScreen
import acekode.debtshare.presentation.home.activity.ActivityUiState
import acekode.debtshare.presentation.home.activity.ActivityViewModel
import acekode.debtshare.presentation.home.groups.GroupsScreen
import acekode.debtshare.presentation.home.groups.balances.BalancesScreen
import acekode.debtshare.presentation.home.groups.balances.BalancesViewModel
import acekode.debtshare.presentation.home.groups.groupdetail.GroupDetailScreen
import acekode.debtshare.presentation.home.groups.invitations.InvitationScreen
import acekode.debtshare.presentation.home.profile.ProfileScreen
import acekode.debtshare.ui.items.DebtshareBellDefaults
import acekode.debtshare.ui.items.DebtshareBottomSheet
import acekode.debtshare.ui.items.DebtshareIcon
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route

    val activityViewModel = koinViewModel<ActivityViewModel>()
    val activityUiState by activityViewModel.uiState.collectAsStateWithLifecycle()
    val activityBadgeCount = (activityUiState as? ActivityUiState.Content)?.unreadCount ?: 0

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            HomeBottomBar(
                currentRoute = currentRoute,
                activityBadgeCount = activityBadgeCount,
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
                    onGroupClick = { groupId ->
                        navController.navigate(Screen.Home.GroupDetail.createRoute(groupId))
                    },
                )
            }
            composable(
                route = Screen.Home.GroupDetail.route,
                arguments = listOf(
                    navArgument("groupId") { type = NavType.StringType },
                ),
            ) { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId").orEmpty()
                var showInvitation by remember { mutableStateOf(false) }

                GroupDetailScreen(
                    onBackClick = { navController.popBackStack() },
                    onAddExpenseClick = { },
                    onBalancesClick = {
                        navController.navigate(Screen.Home.Balances.createRoute(groupId))
                    },
                    onInviteClick = { showInvitation = true },
                    onMenuClick = { },
                )

                DebtshareBottomSheet(
                    visible = showInvitation,
                    onDismissRequest = { showInvitation = false },
                ) {
                    InvitationScreen(onCloseClick = { showInvitation = false })
                }
            }
            composable(
                route = Screen.Home.Balances.route,
                arguments = listOf(
                    navArgument("groupId") { type = NavType.StringType },
                ),
            ) {
                BalancesDestination(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.Home.Activity.route) {
                ActivityScreen()
            }
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
    activityBadgeCount: Int,
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
                val selected = currentRoute == tab.route ||
                    (
                        tab == HomeTab.Groups &&
                            (
                                currentRoute?.startsWith("group_detail") == true ||
                                    currentRoute?.startsWith("balances") == true
                                )
                        )
                val badgeCount = if (tab == HomeTab.Activity) activityBadgeCount else 0
                BottomBarItem(
                    tab = tab,
                    selected = selected,
                    badgeCount = badgeCount,
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
    badgeCount: Int,
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
        Box {
            DebtshareIcon(
                icon = tab.icon,
                contentDescription = stringResource(tab.label),
                tint = color,
            )
            BottomBarBadge(
                count = badgeCount,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-4).dp),
            )
        }
        Text(
            text = stringResource(tab.label),
            color = color,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}

@Composable
private fun BottomBarBadge(
    count: Int,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = count > 0,
        enter = scaleIn(
            initialScale = DebtshareBellDefaults.ENTER_SCALE,
            animationSpec = tween(DebtshareBellDefaults.ENTER_MILLIS),
        ),
        exit = fadeOut(tween(DebtshareBellDefaults.EXIT_MILLIS)),
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = 14.dp, minHeight = 14.dp)
                .clip(CircleShape)
                .background(DebtshareColors.Semantic.error)
                .padding(horizontal = 3.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (count > DebtshareBellDefaults.MAX_COUNT) {
                    "${DebtshareBellDefaults.MAX_COUNT}+"
                } else {
                    count.toString()
                },
                style = DebtshareTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 9.sp,
                ),
                color = DebtshareColors.Neutral.n0,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun BalancesDestination(onBackClick: () -> Unit) {
    val viewModel = koinViewModel<BalancesViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BalancesScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onConfirmPaymentClick = { },
        onRemindClick = { },
        onSettleClick = { },
    )
}

@DebtshareComponentPreview
@Composable
private fun HomeBottomBarGroupsSelectedPreview() {
    DebtshareTheme(darkTheme = false) {
        HomeBottomBar(
            currentRoute = Screen.Home.Groups.route,
            activityBadgeCount = 5,
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
            activityBadgeCount = 5,
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
            activityBadgeCount = 5,
            onNavigate = {},
        )
    }
}
