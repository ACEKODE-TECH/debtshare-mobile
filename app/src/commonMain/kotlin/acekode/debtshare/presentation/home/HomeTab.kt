package acekode.debtshare.presentation.home

import acekode.debtshare.navigation.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.tab_activity
import debtshare.app.generated.resources.tab_home
import debtshare.app.generated.resources.tab_profile
import org.jetbrains.compose.resources.StringResource

internal enum class HomeTab(
    val route: String,
    val label: StringResource,
    val icon: ImageVector,
) {
    Groups(
        route = Screen.Home.Groups.route,
        label = Res.string.tab_home,
        icon = Icons.Default.Home,
    ),
    Activity(
        route = Screen.Home.Activity.route,
        label = Res.string.tab_activity,
        icon = Icons.Default.Notifications,
    ),
    Profile(
        route = Screen.Home.Profile.route,
        label = Res.string.tab_profile,
        icon = Icons.Default.Person,
    ),
}
