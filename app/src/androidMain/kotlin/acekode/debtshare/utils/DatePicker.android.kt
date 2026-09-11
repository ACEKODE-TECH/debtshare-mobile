package acekode.debtshare.utils

import android.app.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
actual fun rememberDatePickerLauncher(onDateSelected: (String) -> Unit): () -> Unit {
    val context = LocalContext.current
    return remember(onDateSelected) {
        {
            val now = Calendar.getInstance()
            val dialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selected = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
                    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    onDateSelected(formatter.format(selected.time))
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
            )
            dialog.datePicker.maxDate = now.timeInMillis
            dialog.show()
        }
    }
}
