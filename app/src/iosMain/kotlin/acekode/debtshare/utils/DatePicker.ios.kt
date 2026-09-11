package acekode.debtshare.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.UIKit.UIApplication
import platform.UIKit.UIBarButtonItem
import platform.UIKit.UIBarButtonSystemItemCancel
import platform.UIKit.UIBarButtonSystemItemDone
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIDatePickerStyle
import platform.UIKit.UIModalPresentationStyle
import platform.UIKit.UINavigationController
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import platform.objc.sel_registerName

@Composable
actual fun rememberDatePickerLauncher(onDateSelected: (String) -> Unit): () -> Unit {
    val handler = remember { DatePickerHandler(onDateSelected) }
    return {
        handler.callback = onDateSelected
        handler.show()
    }
}

@OptIn(ExperimentalForeignApi::class)
private class DatePickerHandler(
    var callback: (String) -> Unit,
) : NSObject() {

    private var datePicker: UIDatePicker? = null
    private var presentingVC: UIViewController? = null

    fun show() {
        val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return
        val topVC = findTopViewController(rootVC)

        val picker = UIDatePicker().apply {
            datePickerMode = UIDatePickerMode.UIDatePickerModeDate
            preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleInline
            maximumDate = NSDate()
            translatesAutoresizingMaskIntoConstraints = false
        }
        datePicker = picker

        val vc = UIViewController()
        vc.view.backgroundColor = platform.UIKit.UIColor.systemBackgroundColor
        vc.view.addSubview(picker)

        platform.Foundation.NSLayoutConstraint.activateConstraints(
            listOf(
                picker.topAnchor.constraintEqualToAnchor(
                    vc.view.safeAreaLayoutGuide.topAnchor,
                    constant = 16.0,
                ),
                picker.centerXAnchor.constraintEqualToAnchor(vc.view.centerXAnchor),
            ),
        )

        vc.navigationItem.leftBarButtonItem = UIBarButtonItem(
            barButtonSystemItem = UIBarButtonSystemItemCancel,
            target = this,
            action = sel_registerName("onCancel"),
        )
        vc.navigationItem.rightBarButtonItem = UIBarButtonItem(
            barButtonSystemItem = UIBarButtonSystemItemDone,
            target = this,
            action = sel_registerName("onDone"),
        )

        val nav = UINavigationController(rootViewController = vc)
        nav.modalPresentationStyle = UIModalPresentationStyle.UIModalPresentationPageSheet
        presentingVC = topVC
        topVC.presentViewController(nav, animated = true, completion = null)
    }

    @Suppress("unused")
    fun onCancel() {
        presentingVC?.dismissViewControllerAnimated(true, completion = null)
    }

    @Suppress("unused")
    fun onDone() {
        val picker = datePicker ?: return
        val formatter = NSDateFormatter()
        formatter.dateFormat = "dd MMM yyyy"
        callback(formatter.stringFromDate(picker.date))
        presentingVC?.dismissViewControllerAnimated(true, completion = null)
    }
}

private fun findTopViewController(vc: UIViewController): UIViewController {
    vc.presentedViewController?.let { return findTopViewController(it) }
    (vc as? UINavigationController)?.visibleViewController?.let { return findTopViewController(it) }
    return vc
}
