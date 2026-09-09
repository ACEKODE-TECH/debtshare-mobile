package acekode.debtshare.presentation.home.groups

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.DrawableResource

@Immutable
data class GroupSummaryUiModel(
    val id: String,
    val name: String,
    val icon: DrawableResource,
    val iconTint: Color,
    val memberCount: Int,
    val expenseCount: Int,
    val balance: GroupBalance,
    val hasActivity: Boolean,
    val members: ImmutableList<MemberBadge>,
)

@Immutable
data class MemberBadge(
    val initials: String,
    val color: Color,
)

@Immutable
sealed interface GroupBalance {
    data class Positive(val formattedAmount: String) : GroupBalance
    data class Negative(val formattedAmount: String) : GroupBalance
    data object Settled : GroupBalance
}

enum class GroupFilter { All, WithActivity, Archived }
