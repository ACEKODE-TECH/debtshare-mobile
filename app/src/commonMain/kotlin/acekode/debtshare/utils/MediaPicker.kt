package acekode.debtshare.utils

import androidx.compose.runtime.Composable

@Composable
expect fun rememberCameraLauncher(onResult: (ByteArray?) -> Unit): () -> Unit

@Composable
expect fun rememberFilePickerLauncher(onResult: (ByteArray?) -> Unit): () -> Unit
