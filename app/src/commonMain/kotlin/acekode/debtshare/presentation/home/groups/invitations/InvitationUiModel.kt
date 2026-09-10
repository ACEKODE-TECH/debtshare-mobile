package acekode.debtshare.presentation.home.groups.invitations

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

enum class InvitationTab { Link, Alias }

@Immutable
sealed interface UserInvitationStatus {
    data object Available : UserInvitationStatus
    data object InGroup : UserInvitationStatus
    data object Pending : UserInvitationStatus
}

@Immutable
data class UserSearchResultUiModel(
    val id: String,
    val name: String,
    val alias: String,
    val subtitle: String,
    val avatarColor: Color,
    val status: UserInvitationStatus,
)
