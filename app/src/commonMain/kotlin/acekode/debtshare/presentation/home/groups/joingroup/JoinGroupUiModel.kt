package acekode.debtshare.presentation.home.groups.joingroup

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class JoinGroupMemberUiModel(
    val name: String,
    val avatarColor: Color,
)

@Immutable
data class GroupTagsUiModel(
    val currency: String,
    val expenseCount: Int,
    val isActive: Boolean,
)
