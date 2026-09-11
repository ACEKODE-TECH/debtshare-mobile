package acekode.debtshare.presentation.home.groups.addexpense

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ExpenseReviewViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val mode: String = savedStateHandle["mode"] ?: "manual"

    val uiState: StateFlow<ExpenseReviewUiState>
        field = MutableStateFlow(initialState())

    init {
        if (mode == "scan") {
            viewModelScope.launch {
                delay(ANALYSIS_DURATION_MS)
                uiState.update { mockScanResult() }
            }
        }
    }

    fun onFieldChanged(field: String, value: String) {
        val current = (uiState.value as? ExpenseReviewUiState.Content) ?: return
        val data = current.data
        val updated = when (field) {
            "amount" -> data.copy(amount = sanitizeAmount(value))
            "description" -> data.copy(description = value)
            "category" -> data.copy(category = value)
            "date" -> data.copy(date = value)
            "paidBy" -> data.copy(paidByName = value)
            else -> return
        }
        uiState.update { current.copy(data = updated) }
    }

    fun onToggleMember(memberId: String) {
        val current = (uiState.value as? ExpenseReviewUiState.Content) ?: return
        val updated = current.data.members.map { member ->
            if (member.id == memberId) member.copy(selected = !member.selected) else member
        }.toImmutableList()
        uiState.update { current.copy(data = current.data.copy(members = updated)) }
    }

    private fun initialState(): ExpenseReviewUiState = when (mode) {
        "scan" -> ExpenseReviewUiState.Analyzing
        else -> ExpenseReviewUiState.Content(emptyForm())
    }

    companion object {
        private const val ANALYSIS_DURATION_MS = 3_000L
    }
}

private fun mockScanResult() = ExpenseReviewUiState.Content(
    data = ExpenseFormData(
        amount = "47,80",
        description = "Compra Mercadona",
        category = "Compra",
        date = "12 ago 2026",
        paidByName = "Ana (Tú)",
        members = persistentListOf(
            ExpenseMemberUiModel("m1", "Ana", null, true),
            ExpenseMemberUiModel("m2", "Carlos", null, true),
            ExpenseMemberUiModel("m3", "Marta", null, true),
            ExpenseMemberUiModel("m4", "Luis", null, true),
        ),
        perPersonAmount = "11,95",
        attachmentName = "ticket-mercadona.jpg",
        attachmentSize = "1,2 MB",
        isFromScan = true,
    ),
)

private fun sanitizeAmount(input: String): String {
    val normalized = input.replace('.', ',')
    val sb = StringBuilder()
    var hasComma = false
    var intCount = 0
    var decCount = 0
    for (c in normalized) {
        when {
            c.isDigit() && !hasComma && intCount < MAX_INT_DIGITS -> {
                sb.append(c)
                intCount++
            }

            c.isDigit() && hasComma && decCount < MAX_DEC_DIGITS -> {
                sb.append(c)
                decCount++
            }

            c == ',' && !hasComma -> {
                hasComma = true
                sb.append(',')
            }
        }
    }
    if (sb.isEmpty()) return ""
    val result = sb.toString()
    val commaPos = result.indexOf(',')
    val intRaw = if (commaPos >= 0) result.substring(0, commaPos) else result
    val decRaw = if (commaPos >= 0) result.substring(commaPos) else ""
    val intClean = intRaw.trimStart('0').ifEmpty { "0" }
    return "$intClean$decRaw"
}

private const val MAX_INT_DIGITS = 10
private const val MAX_DEC_DIGITS = 2

private fun emptyForm() = ExpenseFormData(
    amount = "",
    description = "",
    category = "",
    date = "",
    paidByName = "",
    members = persistentListOf(
        ExpenseMemberUiModel("m1", "Ana", null, true),
        ExpenseMemberUiModel("m2", "Carlos", null, true),
        ExpenseMemberUiModel("m3", "Marta", null, true),
        ExpenseMemberUiModel("m4", "Luis", null, true),
    ),
    perPersonAmount = "",
    attachmentName = "",
    attachmentSize = "",
    isFromScan = false,
)
