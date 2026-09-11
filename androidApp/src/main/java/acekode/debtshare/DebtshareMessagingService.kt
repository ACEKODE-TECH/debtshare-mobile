package acekode.debtshare

import acekode.debtshare.navigation.GroupInviteStats
import acekode.debtshare.navigation.PendingNotification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DebtshareMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        when (data["type"]) {
            "group_invite" -> handleGroupInvite(data)
            "group_invite_expired" -> handleExpiredGroupInvite(data)
        }
    }

    private fun handleGroupInvite(data: Map<String, String>) {
        val inviteCode = data["invite_code"] ?: return
        val groupName = data["group_name"] ?: return

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_NOTIFICATION_TYPE, "group_invite")
            putExtra(EXTRA_INVITE_CODE, inviteCode)
            putExtra(EXTRA_GROUP_NAME, groupName)
            putExtra(EXTRA_GROUP_DESCRIPTION, data["description"].orEmpty())
            putExtra(EXTRA_INVITER_NAME, data["inviter_name"].orEmpty())
            putExtra(EXTRA_MEMBERS_COUNT, data["members_count"]?.toIntOrNull() ?: 0)
            putExtra(EXTRA_CURRENCY, data["currency"].orEmpty())
            putExtra(EXTRA_EXPENSE_COUNT, data["expense_count"]?.toIntOrNull() ?: 0)
            putExtra(EXTRA_IS_ACTIVE, data["is_active"]?.toBooleanStrictOrNull() ?: true)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            inviteCode.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        ensureNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(groupName)
            .setContentText(getString(R.string.notification_invite_body, data["inviter_name"].orEmpty()))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(inviteCode.hashCode(), notification)
    }

    private fun handleExpiredGroupInvite(data: Map<String, String>) {
        val inviteCode = data["invite_code"] ?: return
        val groupName = data["group_name"] ?: return

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_NOTIFICATION_TYPE, "group_invite_expired")
            putExtra(EXTRA_INVITE_CODE, inviteCode)
            putExtra(EXTRA_GROUP_NAME, groupName)
            putExtra(EXTRA_GENERATED_BY, data["generated_by"].orEmpty())
            putExtra(EXTRA_EXPIRED_AGO, data["expired_ago"].orEmpty())
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            inviteCode.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        ensureNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(groupName)
            .setContentText(getString(R.string.notification_expired_body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(inviteCode.hashCode(), notification)
    }

    private fun ensureNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_invites),
                NotificationManager.IMPORTANCE_HIGH,
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "group_invites"
        private const val EXTRA_NOTIFICATION_TYPE = "extra_notification_type"
        private const val EXTRA_INVITE_CODE = "extra_invite_code"
        private const val EXTRA_GROUP_NAME = "extra_group_name"
        private const val EXTRA_GROUP_DESCRIPTION = "extra_group_description"
        private const val EXTRA_INVITER_NAME = "extra_inviter_name"
        private const val EXTRA_MEMBERS_COUNT = "extra_members_count"
        private const val EXTRA_CURRENCY = "extra_currency"
        private const val EXTRA_EXPENSE_COUNT = "extra_expense_count"
        private const val EXTRA_IS_ACTIVE = "extra_is_active"
        private const val EXTRA_GENERATED_BY = "extra_generated_by"
        private const val EXTRA_EXPIRED_AGO = "extra_expired_ago"

        fun extractNotification(intent: Intent): PendingNotification? {
            val type = intent.getStringExtra(EXTRA_NOTIFICATION_TYPE)
                ?: intent.getStringExtra("type")
            val inviteCode = intent.getStringExtra(EXTRA_INVITE_CODE)
                ?: intent.getStringExtra("invite_code")
                ?: return null
            val groupName = intent.getStringExtra(EXTRA_GROUP_NAME)
                ?: intent.getStringExtra("group_name")
                ?: return null

            return when (type) {
                "group_invite" -> extractGroupInvite(intent, inviteCode, groupName)
                "group_invite_expired" -> extractExpiredInvite(intent, inviteCode, groupName)
                else -> null
            }
        }

        private fun extractGroupInvite(
            intent: Intent,
            inviteCode: String,
            groupName: String,
        ) = PendingNotification.GroupInvite(
            inviteCode = inviteCode,
            groupName = groupName,
            groupDescription = intent.dualString(EXTRA_GROUP_DESCRIPTION, "description"),
            inviterName = intent.dualString(EXTRA_INVITER_NAME, "inviter_name"),
            membersCount = intent.dualInt(EXTRA_MEMBERS_COUNT, "members_count"),
            stats = GroupInviteStats(
                currency = intent.dualString(EXTRA_CURRENCY, "currency"),
                expenseCount = intent.dualInt(EXTRA_EXPENSE_COUNT, "expense_count"),
                isActive = intent.dualBoolean(EXTRA_IS_ACTIVE, "is_active", true),
            ),
        )

        private fun extractExpiredInvite(
            intent: Intent,
            inviteCode: String,
            groupName: String,
        ) = PendingNotification.ExpiredGroupInvite(
            inviteCode = inviteCode,
            groupName = groupName,
            generatedBy = intent.dualString(EXTRA_GENERATED_BY, "generated_by"),
            expiredAgo = intent.dualString(EXTRA_EXPIRED_AGO, "expired_ago"),
        )

        private fun Intent.dualString(
            prefixed: String,
            raw: String,
        ): String = (getStringExtra(prefixed) ?: getStringExtra(raw)).orEmpty()

        private fun Intent.dualInt(prefixed: String, raw: String): Int {
            val value = getIntExtra(prefixed, -1)
            if (value >= 0) return value
            return getStringExtra(raw)?.toIntOrNull() ?: 0
        }

        private fun Intent.dualBoolean(
            prefixed: String,
            raw: String,
            default: Boolean,
        ): Boolean = if (hasExtra(prefixed)) {
            getBooleanExtra(prefixed, default)
        } else {
            getStringExtra(raw)?.toBooleanStrictOrNull() ?: default
        }
    }
}
