package acekode.debtshare.utils

import platform.Foundation.NSLog

actual fun logDebug(tag: String, message: String) {
    NSLog("[$tag] $message")
}

actual fun logError(tag: String, message: String, throwable: Throwable?) {
    val suffix = throwable?.let { " — ${it.message}" }.orEmpty()
    NSLog("[$tag] ERROR: $message$suffix")
}
