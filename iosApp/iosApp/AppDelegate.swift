import UIKit
import FirebaseMessaging
import UserNotifications
import shared

final class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        UNUserNotificationCenter.current().delegate = self
        Messaging.messaging().delegate = self

        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { _, _ in }
        application.registerForRemoteNotifications()

        return true
    }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        // Will send to backend when available
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        completionHandler([.banner, .badge, .sound])
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        let userInfo = response.notification.request.content.userInfo
        handleNotificationData(userInfo)
        completionHandler()
    }

    private func handleNotificationData(_ userInfo: [AnyHashable: Any]) {
        guard let type = userInfo["type"] as? String else { return }
        guard let inviteCode = userInfo["invite_code"] as? String else { return }
        guard let groupName = userInfo["group_name"] as? String else { return }

        switch type {
        case "group_invite":
            let stats = GroupInviteStats(
                currency: userInfo["currency"] as? String ?? "",
                expenseCount: Int32(userInfo["expense_count"] as? String ?? "0") ?? 0,
                isActive: (userInfo["is_active"] as? String ?? "true") == "true"
            )
            let notification = PendingNotificationGroupInvite(
                inviteCode: inviteCode,
                groupName: groupName,
                groupDescription: userInfo["description"] as? String ?? "",
                inviterName: userInfo["inviter_name"] as? String ?? "",
                membersCount: Int32(userInfo["members_count"] as? String ?? "0") ?? 0,
                stats: stats
            )
            NotificationRouter.shared.post(notification: notification)

        case "group_invite_expired":
            let notification = PendingNotificationExpiredGroupInvite(
                inviteCode: inviteCode,
                groupName: groupName,
                generatedBy: userInfo["generated_by"] as? String ?? "",
                expiredAgo: userInfo["expired_ago"] as? String ?? ""
            )
            NotificationRouter.shared.post(notification: notification)

        default:
            break
        }
    }
}
