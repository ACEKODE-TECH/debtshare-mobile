package acekode.debtshare.presentation.home.groups.addexpense

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ExpenseReviewUiState {
    data object Analyzing : ExpenseReviewUiState
    data class Content(val data: ExpenseFormData) : ExpenseReviewUiState {
        override fun toString(): String = "Content"
    }
}
