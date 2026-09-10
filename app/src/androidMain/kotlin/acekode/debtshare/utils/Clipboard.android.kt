package acekode.debtshare.utils

import acekode.debtshare.AppContext
import android.content.ClipData
import android.content.ClipboardManager

actual fun copyToClipboard(text: String) {
    val clipboard = AppContext.context
        .getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("", text))
}
