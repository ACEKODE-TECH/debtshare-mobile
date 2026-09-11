package acekode.debtshare.presentation.home.groups.invitations

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface InvitationUiState {
    data object Loading : InvitationUiState

    data class Content(
        val groupName: String,
        val memberCount: Int,
        val invitationLink: String,
        val searchResults: ImmutableList<UserSearchResultUiModel>,
    ) : InvitationUiState
}
