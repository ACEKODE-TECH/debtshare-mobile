package acekode.debtshare.presentation.home.activity

import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.utils.logDebug
import androidx.lifecycle.ViewModel
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.bag
import debtshare.app.generated.resources.check_circle
import debtshare.app.generated.resources.cup
import debtshare.app.generated.resources.house
import debtshare.app.generated.resources.search
import debtshare.app.generated.resources.star
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ActivityViewModel : ViewModel() {

    val uiState: StateFlow<ActivityUiState>
        field = MutableStateFlow<ActivityUiState>(mockContent())

    val selectedTab: StateFlow<ActivityTab>
        field = MutableStateFlow(ActivityTab.Activity)

    fun onTabSelected(tab: ActivityTab) {
        selectedTab.value = tab
    }

    fun onMarkReadClick() {
        val current = uiState.value as? ActivityUiState.Content ?: return
        uiState.value = current.copy(
            activitySections = current.activitySections.map { section ->
                section.copy(
                    items = section.items.map { it.copy(isUnread = false) }.toPersistentList(),
                )
            }.toPersistentList(),
            unreadCount = 0,
        )
    }

    fun onAcceptInvitation(id: String) {
        logDebug("ActivityViewModel", "Invitation accepted: $id")
    }

    fun onDeclineInvitation(id: String) {
        logDebug("ActivityViewModel", "Invitation declined: $id")
    }
}

private fun mockContent(): ActivityUiState.Content = ActivityUiState.Content(
    activityCount = 3,
    invitationCount = 2,
    unreadCount = 5,
    activitySections = mockActivitySections(),
    invitations = mockInvitations(),
    recentInvitations = persistentListOf(
        RecentInvitationUiModel(
            id = "recent1",
            groupName = "Piso Castellana 43",
            groupIcon = Res.drawable.house,
            groupIconTint = DebtshareColors.Brand.primary,
            groupIconContainer = DebtshareColors.Brand.primaryTint,
            acceptedTimeText = "aceptaste hace 3 días",
        ),
    ),
)

private fun mockActivitySections() = persistentListOf(
    ActivitySectionUiModel(
        title = "HOY",
        items = persistentListOf(
            ActivityItemUiModel(
                id = "1",
                avatarName = "Carlos Mena",
                description = "Carlos añadió Compra Mercadona en Piso Castellana 43",
                actionIcon = Res.drawable.bag,
                actionIconTint = DebtshareColors.Accent.mustardDark,
                actionIconContainer = DebtshareColors.Accent.mustardTint,
                detailText = "-11,95 €",
                detailColor = DebtshareColors.Semantic.error,
                timeText = "hace 12 min",
                isUnread = true,
            ),
            ActivityItemUiModel(
                id = "2",
                avatarName = "Luis García",
                description = "Luis confirmó tu pago de 12,00 €",
                actionIcon = Res.drawable.check_circle,
                actionIconTint = DebtshareColors.Semantic.success,
                actionIconContainer = DebtshareColors.Semantic.successTintSoft,
                detailText = "Liquidado ✓",
                detailColor = DebtshareColors.Semantic.success,
                timeText = "hace 1 h",
                isUnread = false,
            ),
            ActivityItemUiModel(
                id = "3",
                avatarName = "Marta López",
                description = "Marta añadió Cena japonesa en Piso Castellana 43",
                actionIcon = Res.drawable.cup,
                actionIconTint = DebtshareColors.Accent.plum,
                actionIconContainer = DebtshareColors.Accent.plumTint,
                detailText = "-15,20 €",
                detailColor = DebtshareColors.Semantic.error,
                timeText = "hace 3 h",
                isUnread = false,
            ),
        ),
    ),
    ActivitySectionUiModel(
        title = "AYER",
        items = persistentListOf(
            ActivityItemUiModel(
                id = "4",
                avatarName = "Carlos Mena",
                description = "Carlos escaneó un ticket de Netflix",
                actionIcon = Res.drawable.search,
                actionIconTint = DebtshareColors.Brand.primary,
                actionIconContainer = DebtshareColors.Brand.primaryTint,
                detailText = "-3,32 €",
                detailColor = DebtshareColors.Semantic.error,
                timeText = "ayer 21:14",
                isUnread = false,
            ),
            ActivityItemUiModel(
                id = "5",
                avatarName = "Jorge Sanzo",
                description = "Enviaste una transferencia a Luis",
                actionIcon = Res.drawable.star,
                actionIconTint = DebtshareColors.Brand.primary,
                actionIconContainer = DebtshareColors.Brand.primaryTint,
                detailText = "12,00 €",
                detailColor = DebtshareColors.Brand.primary,
                timeText = "ayer 18:02",
                isUnread = false,
            ),
        ),
    ),
)

private fun mockInvitations() = persistentListOf(
    InvitationUiModel(
        id = "inv1",
        groupName = "Viaje Roma 2026",
        groupIcon = Res.drawable.star,
        groupIconTint = DebtshareColors.Brand.primary,
        groupIconContainer = DebtshareColors.Brand.primaryTint,
        memberCount = 6,
        groupType = "viaje",
        inviterName = "Elena",
        inviteTimeText = "hace 30 min",
        members = persistentListOf("Elena", "Nuria", "Pablo"),
        isNew = true,
    ),
    InvitationUiModel(
        id = "inv2",
        groupName = "Cena cumpleaños Pablo",
        groupIcon = Res.drawable.cup,
        groupIconTint = DebtshareColors.Accent.plum,
        groupIconContainer = DebtshareColors.Accent.plumTint,
        memberCount = 8,
        groupType = "cena",
        inviterName = "Pablo",
        inviteTimeText = "ayer",
        members = persistentListOf(),
        isNew = false,
    ),
)
