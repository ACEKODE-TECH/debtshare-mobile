package acekode.debtshare.presentation.home.profile

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Error(val message: String) : ProfileUiState
    data class Content(
        val user: ProfileUser,
        val balance: ProfileBalance,
        val analytics: ProfileAnalytics,
        val categories: ImmutableList<ProfileCategory>,
        val groupComparisons: ImmutableList<ProfileGroupComparison>,
        val account: ProfileAccount,
    ) : ProfileUiState

    val isLoading: Boolean get() = this is Loading
    val errorMessage: String? get() = (this as? Error)?.message
}
