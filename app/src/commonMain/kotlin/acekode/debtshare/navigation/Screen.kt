package acekode.debtshare.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Signup : Screen("signup_screen")
    object GoogleAlias : Screen("google_alias_screen")
    object Home : Screen("home_screen") {
        object Groups : Screen("groups")
        object Activity : Screen("activity")
        object Profile : Screen("profile")
        object GroupDetail : Screen("group_detail/{groupId}") {
            fun createRoute(groupId: String) = "group_detail/$groupId"
        }
        object Invitation : Screen("invitation/{groupId}") {
            fun createRoute(groupId: String) = "invitation/$groupId"
        }
    }
}
