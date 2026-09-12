package acekode.debtshare.presentation.home

import acekode.debtshare.navigation.Screen
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.bell
import debtshare.app.generated.resources.house
import debtshare.app.generated.resources.person
import debtshare.app.generated.resources.tab_activity
import debtshare.app.generated.resources.tab_home
import debtshare.app.generated.resources.tab_profile
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

internal enum class HomeTab(
    val route: String,
    val label: StringResource,
    val icon: DrawableResource,
) {
    Groups(
        route = Screen.Home.Groups.route,
        label = Res.string.tab_home,
        icon = Res.drawable.house,
    ),
    Activity(
        route = Screen.Home.Activity.route,
        label = Res.string.tab_activity,
        icon = Res.drawable.bell,
    ),
    Profile(
        route = Screen.Home.Profile.route,
        label = Res.string.tab_profile,
        icon = Res.drawable.person,
    ),
}
