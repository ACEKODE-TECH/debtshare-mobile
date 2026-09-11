package acekode.debtshare.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class GroupInviteStats(
    val currency: String,
    val expenseCount: Int,
    val isActive: Boolean,
)

sealed interface PendingNotification {

    data class GroupInvite(
        val inviteCode: String,
        val groupName: String,
        val groupDescription: String,
        val inviterName: String,
        val membersCount: Int,
        val stats: GroupInviteStats,
    ) : PendingNotification

    data class ExpiredGroupInvite(
        val inviteCode: String,
        val groupName: String,
        val generatedBy: String,
        val expiredAgo: String,
    ) : PendingNotification
}

object NotificationRouter {
    private val _pending = MutableStateFlow<PendingNotification?>(null)
    val pending: StateFlow<PendingNotification?> = _pending

    fun post(notification: PendingNotification) {
        _pending.value = notification
    }

    fun consume() {
        _pending.value = null
    }
}
