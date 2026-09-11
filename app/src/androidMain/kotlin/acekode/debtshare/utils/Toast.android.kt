package acekode.debtshare.utils

import acekode.debtshare.AppContext
import android.widget.Toast

actual fun showToast(message: String) {
    Toast.makeText(AppContext.context, message, Toast.LENGTH_SHORT).show()
}
