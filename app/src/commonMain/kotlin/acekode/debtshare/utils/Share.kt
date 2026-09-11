package acekode.debtshare.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

expect fun shareText(text: String, title: String = "")

@Composable
expect fun NativeShareTargets(text: String, modifier: Modifier = Modifier)
