package acekode.debtshare.ui.utils

import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
annotation class DebtshareComponentPreview

@Preview(
    name = "Small Phone",
    device = "spec:width=360dp,height=640dp,dpi=480",
    apiLevel = 36,
    showBackground = true,
)
@Preview(
    name = "Medium Phone LTR",
    device = "spec:width=411dp,height=891dp,dpi=420",
    apiLevel = 36,
    showBackground = true,
    locale = "es",
)
@Preview(
    name = "Medium Phone RTL",
    device = "spec:width=411dp,height=891dp,dpi=420",
    apiLevel = 36,
    showBackground = true,
    locale = "ar",
)
@Preview(
    name = "Large Phone",
    device = "spec:width=600dp,height=1024dp,dpi=480",
    apiLevel = 36,
    showBackground = true,
)
annotation class DebtshareScreenPreview
