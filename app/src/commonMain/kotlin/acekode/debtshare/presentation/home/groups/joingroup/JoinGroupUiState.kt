package acekode.debtshare.presentation.home.groups.joingroup

import kotlinx.collections.immutable.ImmutableList

sealed interface JoinGroupUiState {

    data object Loading : JoinGroupUiState

    data class Valid(
        val inviteCode: String,
        val groupName: String,
        val groupDescription: String,
        val inviterName: String,
        val members: ImmutableList<JoinGroupMemberUiModel>,
        val membersCount: Int = members.size,
        val tags: GroupTagsUiModel,
    ) : JoinGroupUiState {

        override fun toString(): String = "Valid"
    }

    data class Expired(
        val inviteCode: String,
        val groupName: String,
        val generatedBy: String,
        val expiredAgo: String,
    ) : JoinGroupUiState {

        override fun toString(): String = "Expired"
    }
}
