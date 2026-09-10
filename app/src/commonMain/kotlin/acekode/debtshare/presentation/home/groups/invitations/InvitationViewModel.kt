package acekode.debtshare.presentation.home.groups.invitations

import acekode.debtshare.ui.theme.DebtshareColors
import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class InvitationViewModel : ViewModel() {

    val uiState: StateFlow<InvitationUiState>
        field = MutableStateFlow<InvitationUiState>(mockContent())

    val selectedTab: StateFlow<InvitationTab>
        field = MutableStateFlow(InvitationTab.Link)

    val searchQuery: StateFlow<String>
        field = MutableStateFlow("")

    val linkCopied: StateFlow<Boolean>
        field = MutableStateFlow(false)

    fun onTabSelected(tab: InvitationTab) {
        selectedTab.value = tab
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
        val content = uiState.value as? InvitationUiState.Content ?: return
        val filtered = if (query.isBlank()) {
            persistentListOf()
        } else {
            allUsers.filter {
                it.name.contains(query, ignoreCase = true) ||
                    it.alias.contains(query, ignoreCase = true)
            }.toImmutableList()
        }
        uiState.value = content.copy(searchResults = filtered)
    }

    fun onCopyLink() {
        linkCopied.value = true
    }

    fun onRegenerateLink() {
        linkCopied.value = false
    }

    fun onInviteUser(userId: String) {
        val content = uiState.value as? InvitationUiState.Content ?: return
        val updated = content.searchResults.map { user ->
            if (user.id == userId && user.status is UserInvitationStatus.Available) {
                user.copy(status = UserInvitationStatus.Pending)
            } else {
                user
            }
        }.toImmutableList()
        uiState.value = content.copy(searchResults = updated)
    }
}

private val allUsers = listOf(
    UserSearchResultUiModel(
        id = "u1",
        name = "Lucía Marín",
        alias = "@lucia",
        subtitle = "3 grupos en común",
        avatarColor = DebtshareColors.Accent.violet,
        status = UserInvitationStatus.Available,
    ),
    UserSearchResultUiModel(
        id = "u2",
        name = "Lucía Bernal",
        alias = "@luciab",
        subtitle = "Debtshare",
        avatarColor = DebtshareColors.Accent.plum,
        status = UserInvitationStatus.InGroup,
    ),
    UserSearchResultUiModel(
        id = "u3",
        name = "Luciano Prat",
        alias = "@lucianop",
        subtitle = "1 grupo",
        avatarColor = DebtshareColors.Semantic.success,
        status = UserInvitationStatus.Pending,
    ),
    UserSearchResultUiModel(
        id = "u4",
        name = "Ana Gómez",
        alias = "@anag",
        subtitle = "2 grupos en común",
        avatarColor = DebtshareColors.Brand.primary,
        status = UserInvitationStatus.Available,
    ),
    UserSearchResultUiModel(
        id = "u5",
        name = "Carlos Ruiz",
        alias = "@carlos",
        subtitle = "Debtshare",
        avatarColor = DebtshareColors.Accent.mustardDark,
        status = UserInvitationStatus.Available,
    ),
    UserSearchResultUiModel(
        id = "u6",
        name = "María Torres",
        alias = "@mariat",
        subtitle = "5 grupos en común",
        avatarColor = DebtshareColors.Accent.plum,
        status = UserInvitationStatus.InGroup,
    ),
    UserSearchResultUiModel(
        id = "u7",
        name = "Pablo Fernández",
        alias = "@pablof",
        subtitle = "1 grupo",
        avatarColor = DebtshareColors.Semantic.success,
        status = UserInvitationStatus.Available,
    ),
    UserSearchResultUiModel(
        id = "u8",
        name = "Jorge Sánchez",
        alias = "@jorges",
        subtitle = "4 grupos en común",
        avatarColor = DebtshareColors.Accent.violet,
        status = UserInvitationStatus.Pending,
    ),
    UserSearchResultUiModel(
        id = "u9",
        name = "Elena Martín",
        alias = "@elena",
        subtitle = "Debtshare",
        avatarColor = DebtshareColors.Brand.secondary,
        status = UserInvitationStatus.Available,
    ),
    UserSearchResultUiModel(
        id = "u10",
        name = "David López",
        alias = "@davidl",
        subtitle = "2 grupos en común",
        avatarColor = DebtshareColors.Accent.mustardDark,
        status = UserInvitationStatus.InGroup,
    ),
)

private fun mockContent(): InvitationUiState.Content = InvitationUiState.Content(
    groupName = "Piso Castellana 43",
    memberCount = 4,
    invitationLink = "debtshare.app/j/pisocast-a7x9",
    searchResults = persistentListOf(),
)
