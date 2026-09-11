package acekode.debtshare.utils

import androidx.compose.runtime.Composable

@Composable
expect fun rememberDatePickerLauncher(onDateSelected: (String) -> Unit): () -> Unit
