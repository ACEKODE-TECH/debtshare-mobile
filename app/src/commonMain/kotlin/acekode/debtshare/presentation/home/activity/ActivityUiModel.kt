package acekode.debtshare.presentation.home.activity

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.DrawableResource

@Immutable
data class ActivityItemUiModel(
    val id: String,
    val avatarName: String,
    val description: String,
    val actionIcon: DrawableResource,
    val actionIconTint: Color,
    val actionIconContainer: Color,
    val detailText: String,
    val detailColor: Color,
    val timeText: String,
    val isUnread: Boolean,
)

@Immutable
data class ActivitySectionUiModel(
    val title: String,
    val items: ImmutableList<ActivityItemUiModel>,
)

@Immutable
data class InvitationUiModel(
    val id: String,
    val groupName: String,
    val groupIcon: DrawableResource,
    val groupIconTint: Color,
    val groupIconContainer: Color,
    val memberCount: Int,
    val groupType: String,
    val inviterName: String,
    val inviteTimeText: String,
    val members: ImmutableList<String>,
    val isNew: Boolean,
)

@Immutable
data class RecentInvitationUiModel(
    val id: String,
    val groupName: String,
    val groupIcon: DrawableResource,
    val groupIconTint: Color,
    val groupIconContainer: Color,
    val acceptedTimeText: String,
)

enum class ActivityTab { Activity, Invitations }
