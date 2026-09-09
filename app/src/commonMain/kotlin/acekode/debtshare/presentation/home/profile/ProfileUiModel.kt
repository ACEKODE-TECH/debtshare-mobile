package acekode.debtshare.presentation.home.profile

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource

@Immutable
data class ProfileUser(
    val name: String,
    val alias: String,
    val groupCount: Int,
    val expenseCount: Int,
)

@Immutable
data class ProfileBalance(
    val total: String,
    val isPositive: Boolean,
    val owed: String,
    val owing: String,
    val thisMonth: String,
)

@Immutable
data class ProfileAnalytics(
    val groupCount: Int,
    val totalSpent: String,
    val spentDelta: String?,
    val isDeltaPositive: Boolean,
    val averageTicket: String,
    val expenseCount: Int,
)

@Immutable
data class ProfileCategory(
    val name: String,
    val icon: DrawableResource,
    val iconTint: Color,
    val barColor: Color,
    val amount: String,
    val percentage: Int,
    val barProgress: Float,
)

@Immutable
sealed interface ProfileGroupBalance {
    data class Positive(val formattedAmount: String) : ProfileGroupBalance
    data class Negative(val formattedAmount: String) : ProfileGroupBalance
    data object Settled : ProfileGroupBalance
}

@Immutable
data class ProfileGroupComparison(
    val id: String,
    val name: String,
    val icon: DrawableResource,
    val iconTint: Color,
    val barColor: Color,
    val memberCount: Int,
    val expenseCount: Int,
    val totalAmount: String,
    val balance: ProfileGroupBalance,
    val barProgress: Float,
)

@Immutable
data class ProfileAccount(
    val paymentMethod: String?,
)
