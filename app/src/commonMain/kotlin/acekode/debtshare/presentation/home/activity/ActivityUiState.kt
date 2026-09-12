package acekode.debtshare.presentation.home.activity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface ActivityUiState {
    data object Loading : ActivityUiState
    data class Error(val message: String) : ActivityUiState
    data class Content(
        val activitySections: ImmutableList<ActivitySectionUiModel>,
        val activityCount: Int,
        val invitations: ImmutableList<InvitationUiModel>,
        val recentInvitations: ImmutableList<RecentInvitationUiModel>,
        val invitationCount: Int,
        val unreadCount: Int,
    ) : ActivityUiState

    val isLoading: Boolean get() = this is Loading
    val errorMessage: String? get() = (this as? Error)?.message
}
