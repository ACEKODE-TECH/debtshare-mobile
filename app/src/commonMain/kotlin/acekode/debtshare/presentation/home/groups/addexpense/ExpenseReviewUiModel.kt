package acekode.debtshare.presentation.home.groups.addexpense

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ExpenseMemberUiModel(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val selected: Boolean,
)

@Immutable
data class ExpenseFormData(
    val amount: String,
    val description: String,
    val category: String,
    val date: String,
    val paidByName: String,
    val members: ImmutableList<ExpenseMemberUiModel>,
    val perPersonAmount: String,
    val attachmentName: String,
    val attachmentSize: String,
    val isFromScan: Boolean,
)
