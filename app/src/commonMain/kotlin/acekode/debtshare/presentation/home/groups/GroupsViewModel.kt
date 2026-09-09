package acekode.debtshare.presentation.home.groups

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class GroupsViewModel : ViewModel() {

    val uiState: StateFlow<GroupsUiState>
        field = MutableStateFlow<GroupsUiState>(GroupsUiState.Empty)
}
