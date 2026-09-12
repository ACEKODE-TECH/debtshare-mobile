package acekode.debtshare.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Signup : Screen("signup_screen")
    object GoogleAlias : Screen("google_alias_screen")
    object JoinGroup : Screen("join_group")
    object Home : Screen("home_screen") {
        object Groups : Screen("groups")
        object Activity : Screen("activity")
        object Profile : Screen("profile")
        object GroupDetail : Screen("group_detail/{groupId}") {
            fun createRoute(groupId: String) = "group_detail/$groupId"
        }
        object Balances : Screen("balances/{groupId}") {
            fun createRoute(groupId: String) = "balances/$groupId"
        }
        object AddExpense : Screen("add_expense/{groupId}") {
            fun createRoute(groupId: String) = "add_expense/$groupId"
        }
        object ExpenseReview : Screen("expense_review/{groupId}/{mode}") {
            fun createRoute(groupId: String, mode: String) = "expense_review/$groupId/$mode"
        }
    }
}
