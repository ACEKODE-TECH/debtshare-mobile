package acekode.debtshare.presentation.home.groups

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface GroupsUiState {
    data object Empty : GroupsUiState
    data object Loading : GroupsUiState
    data class Error(val message: String) : GroupsUiState
    data class Content(
        val groups: ImmutableList<GroupSummaryUiModel>,
        val globalBalance: String,
        val activeGroupCount: Int,
    ) : GroupsUiState

    val isLoading: Boolean get() = this is Loading
    val errorMessage: String? get() = (this as? Error)?.message
}
