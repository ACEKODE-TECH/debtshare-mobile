package acekode.debtshare.presentation.home.groups.addexpense

import acekode.debtshare.utils.logDebug
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AddExpenseViewModel : ViewModel() {

    val groupName: StateFlow<String>
        field = MutableStateFlow("Piso Castellana 43")

    fun onImageCaptured(bytes: ByteArray?) {
        if (bytes == null) return
        logDebug("AddExpense", "Image captured: ${bytes.size} bytes")
    }
}
