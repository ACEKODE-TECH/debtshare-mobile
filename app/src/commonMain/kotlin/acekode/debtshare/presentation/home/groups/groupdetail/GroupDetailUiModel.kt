package acekode.debtshare.presentation.home.groups.groupdetail

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.DrawableResource

enum class GroupDetailTab { Expenses, Activity, Notes }

@Immutable
sealed interface GroupDetailBalance {
    data class Positive(val formattedAmount: String) : GroupDetailBalance
    data class Negative(val formattedAmount: String) : GroupDetailBalance
    data object Settled : GroupDetailBalance
}

@Immutable
data class ExpenseSection(
    val dateLabel: String,
    val items: ImmutableList<ExpenseItemUiModel>,
)

@Immutable
sealed interface ExpenseItemUiModel {
    val id: String

    data class Expense(
        override val id: String,
        val icon: DrawableResource,
        val iconTint: Color,
        val title: String,
        val subtitle: String,
        val totalAmount: String,
        val balanceImpact: BalanceImpact,
    ) : ExpenseItemUiModel

    data class Settlement(
        override val id: String,
        val title: String,
        val subtitle: String,
        val amount: String,
    ) : ExpenseItemUiModel
}

@Immutable
sealed interface BalanceImpact {
    data class Positive(val formattedAmount: String) : BalanceImpact
    data class Negative(val formattedAmount: String) : BalanceImpact
}
