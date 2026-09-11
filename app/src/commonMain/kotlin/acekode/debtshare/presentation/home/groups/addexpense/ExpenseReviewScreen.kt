package acekode.debtshare.presentation.home.groups.addexpense

import acekode.debtshare.ui.items.DebtshareAvatar
import acekode.debtshare.ui.items.DebtshareAvatarSize
import acekode.debtshare.ui.items.DebtshareAvatarState
import acekode.debtshare.ui.items.DebtshareCardField
import acekode.debtshare.ui.items.DebtshareIcon
import acekode.debtshare.ui.items.DebtshareIconSize
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import acekode.debtshare.utils.rememberDatePickerLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.arrow_left
import debtshare.app.generated.resources.calendar
import debtshare.app.generated.resources.check
import debtshare.app.generated.resources.cup
import debtshare.app.generated.resources.file
import debtshare.app.generated.resources.person
import debtshare.app.generated.resources.review_amount_label
import debtshare.app.generated.resources.review_category_label
import debtshare.app.generated.resources.review_date_label
import debtshare.app.generated.resources.review_description_label
import debtshare.app.generated.resources.review_extracted_subtitle
import debtshare.app.generated.resources.review_extracted_title
import debtshare.app.generated.resources.review_ia_badge
import debtshare.app.generated.resources.review_paid_by_label
import debtshare.app.generated.resources.review_per_person
import debtshare.app.generated.resources.review_save
import debtshare.app.generated.resources.review_split_title
import debtshare.app.generated.resources.review_title
import debtshare.app.generated.resources.star
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExpenseReviewScreen(
    uiState: ExpenseReviewUiState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onToggleMember: (String) -> Unit,
    onFieldChanged: (String, String) -> Unit,
) {
    when (uiState) {
        is ExpenseReviewUiState.Analyzing -> ScanAnalysisView()

        is ExpenseReviewUiState.Content -> ReviewContent(
            data = uiState.data,
            onBackClick = onBackClick,
            onSaveClick = onSaveClick,
            onToggleMember = onToggleMember,
            onFieldChanged = onFieldChanged,
        )
    }
}

@Composable
private fun ReviewContent(
    data: ExpenseFormData,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onToggleMember: (String) -> Unit,
    onFieldChanged: (String, String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { focusManager.clearFocus() }
            .verticalScroll(rememberScrollState()),
    ) {
        ReviewTopBar(onBackClick = onBackClick, onSaveClick = onSaveClick)

        if (data.isFromScan) {
            Spacer(Modifier.height(8.dp))
            ExtractedInfoBanner()
        }

        Spacer(Modifier.height(16.dp))

        AmountSection(
            amount = data.amount,
            isFromScan = data.isFromScan,
            onAmountChanged = { onFieldChanged("amount", it) },
        )

        Spacer(Modifier.height(12.dp))

        DebtshareCardField(
            value = data.description,
            onValueChange = { onFieldChanged("description", it) },
            icon = Res.drawable.file,
            label = stringResource(Res.string.review_description_label),
            modifier = Modifier.padding(horizontal = 16.dp),
            trailing = if (data.isFromScan) ({ AiBadge() }) else null,
        )

        Spacer(Modifier.height(8.dp))

        DebtshareCardField(
            value = data.category,
            onValueChange = { onFieldChanged("category", it) },
            icon = Res.drawable.cup,
            label = stringResource(Res.string.review_category_label),
            modifier = Modifier.padding(horizontal = 16.dp),
            trailing = if (data.isFromScan) ({ AiBadge() }) else null,
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DateSelector(
                date = data.date,
                onDateSelected = { onFieldChanged("date", it) },
                modifier = Modifier.weight(1f),
            )
            PaidBySelector(
                paidByName = data.paidByName,
                members = data.members,
                onPaidByChanged = { onFieldChanged("paidBy", it) },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(16.dp))

        SplitSection(
            members = data.members,
            perPersonAmount = data.perPersonAmount,
            onToggleMember = onToggleMember,
        )

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun ReviewTopBar(onBackClick: () -> Unit, onSaveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DebtshareIcon(icon = Res.drawable.arrow_left, onClick = onBackClick)

        Text(
            text = stringResource(Res.string.review_title),
            style = DebtshareTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            color = DebtshareTheme.colors.textPrimary,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )

        Text(
            text = stringResource(Res.string.review_save),
            style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = DebtshareColors.Brand.primary,
            modifier = Modifier.clickable(onClick = onSaveClick),
        )
    }
}

@Composable
private fun ExtractedInfoBanner() {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DebtshareColors.Brand.primaryTint)
            .border(1.dp, DebtshareColors.Brand.primary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DebtshareIcon(
            icon = Res.drawable.star,
            tint = DebtshareColors.Brand.primary,
            size = DebtshareIconSize.Medium,
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = stringResource(Res.string.review_extracted_title),
                style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = DebtshareColors.Neutral.n900,
            )
            Text(
                text = stringResource(Res.string.review_extracted_subtitle),
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareColors.Neutral.n700,
            )
        }
    }
}

@Composable
private fun AmountSection(
    amount: String,
    isFromScan: Boolean,
    onAmountChanged: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { focusRequester.requestFocus() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.review_amount_label),
                style = DebtshareTheme.typography.labelUppercase,
                color = DebtshareTheme.colors.textTertiary,
            )
            if (isFromScan) AiBadge()
        }
        Spacer(Modifier.height(4.dp))
        val amountStyle = DebtshareTheme.typography.displayLarge.copy(
            fontWeight = FontWeight.Bold,
            color = DebtshareTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        BasicTextField(
            value = amount,
            onValueChange = onAmountChanged,
            textStyle = amountStyle,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            visualTransformation = ThousandsSeparatorTransformation,
            cursorBrush = SolidColor(DebtshareColors.Brand.primary),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.Center) {
                    if (amount.isEmpty()) {
                        Text(
                            text = "0,00 €",
                            style = amountStyle,
                            color = DebtshareTheme.colors.textTertiary,
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}

private object ThousandsSeparatorTransformation : VisualTransformation {
    private const val SUFFIX = " €"

    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text
        if (original.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val commaIdx = original.indexOf(',')
        val intPart = if (commaIdx >= 0) original.substring(0, commaIdx) else original
        val rest = if (commaIdx >= 0) original.substring(commaIdx) else ""

        val sb = StringBuilder()
        val origToTrans = IntArray(original.length + 1)
        var dotsInserted = 0
        for (i in intPart.indices) {
            if (i > 0 && (intPart.length - i) % 3 == 0) {
                sb.append('.')
                dotsInserted++
            }
            origToTrans[i] = i + dotsInserted
            sb.append(intPart[i])
        }
        for (i in rest.indices) {
            origToTrans[intPart.length + i] = intPart.length + dotsInserted + i
        }
        origToTrans[original.length] = original.length + dotsInserted
        sb.append(rest)
        val coreLen = sb.length
        sb.append(SUFFIX)
        val formatted = sb.toString()

        val transToOrig = IntArray(formatted.length + 1)
        var oi = 0
        for (ti in 0 until coreLen) {
            transToOrig[ti] = oi
            if (formatted[ti] != '.' || ti >= intPart.length + dotsInserted) oi++
        }
        for (ti in coreLen..formatted.length) {
            transToOrig[ti] = original.length
        }

        return TransformedText(
            AnnotatedString(formatted),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int) = origToTrans[offset.coerceIn(0, original.length)]
                override fun transformedToOriginal(offset: Int) = transToOrig[offset.coerceIn(0, formatted.length)]
            },
        )
    }
}

@Composable
private fun AiBadge() {
    Text(
        text = stringResource(Res.string.review_ia_badge),
        style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
        color = DebtshareColors.Brand.primary,
        modifier = Modifier
            .background(DebtshareColors.Brand.primaryTint, RoundedCornerShape(4.dp))
            .padding(horizontal = 4.dp, vertical = 1.dp),
    )
}

@Composable
private fun DateSelector(
    date: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val launchDatePicker = rememberDatePickerLauncher(onDateSelected = onDateSelected)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DebtshareTheme.colors.card)
            .clickable { launchDatePicker() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DebtshareIcon(
            icon = Res.drawable.calendar,
            tint = DebtshareTheme.colors.textTertiary,
            size = DebtshareIconSize.Medium,
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = stringResource(Res.string.review_date_label),
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textTertiary,
            )
            Text(
                text = date.ifEmpty { "—" },
                style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (date.isEmpty()) {
                    DebtshareTheme.colors.textTertiary
                } else {
                    DebtshareTheme.colors.textPrimary
                },
            )
        }
    }
}

@Composable
private fun PaidBySelector(
    paidByName: String,
    members: ImmutableList<ExpenseMemberUiModel>,
    onPaidByChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DebtshareTheme.colors.card)
                .clickable { expanded = true }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareIcon(
                icon = Res.drawable.person,
                tint = DebtshareTheme.colors.textTertiary,
                size = DebtshareIconSize.Medium,
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = stringResource(Res.string.review_paid_by_label),
                    style = DebtshareTheme.typography.bodySmall,
                    color = DebtshareTheme.colors.textTertiary,
                )
                Text(
                    text = paidByName.ifEmpty { "—" },
                    style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (paidByName.isEmpty()) {
                        DebtshareTheme.colors.textTertiary
                    } else {
                        DebtshareTheme.colors.textPrimary
                    },
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            members.forEach { member ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            DebtshareAvatar(
                                name = member.name,
                                size = DebtshareAvatarSize.Small,
                            )
                            Text(
                                text = member.name,
                                style = DebtshareTheme.typography.bodyMedium,
                            )
                        }
                    },
                    onClick = {
                        onPaidByChanged(member.name)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun SplitSection(
    members: ImmutableList<ExpenseMemberUiModel>,
    perPersonAmount: String,
    onToggleMember: (String) -> Unit,
) {
    val selectedCount = members.count { it.selected }
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DebtshareTheme.colors.card)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.review_split_title, selectedCount),
                style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = DebtshareTheme.colors.textPrimary,
            )
            if (perPersonAmount.isNotEmpty()) {
                Text(
                    text = stringResource(Res.string.review_per_person, perPersonAmount),
                    style = DebtshareTheme.typography.bodySmall,
                    color = DebtshareTheme.colors.textSecondary,
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            members.forEach { member ->
                MemberChip(member = member, onClick = { onToggleMember(member.id) })
            }
        }
    }
}

@Composable
private fun MemberChip(member: ExpenseMemberUiModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            DebtshareAvatar(
                name = member.name,
                size = DebtshareAvatarSize.Large,
                state = if (member.selected) DebtshareAvatarState.Selected else DebtshareAvatarState.Inactive,
            )
            if (member.selected) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(DebtshareColors.Brand.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    DebtshareIcon(
                        icon = Res.drawable.check,
                        tint = DebtshareColors.Neutral.n0,
                        size = DebtshareIconSize.Small,
                    )
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = member.name,
            style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = if (member.selected) {
                DebtshareTheme.colors.textPrimary
            } else {
                DebtshareTheme.colors.textTertiary
            },
        )
    }
}

private fun previewData() = ExpenseFormData(
    amount = "47,80",
    description = "Compra Mercadona",
    category = "Compra",
    date = "12 ago 2026",
    paidByName = "Ana (Tú)",
    members = persistentListOf(
        ExpenseMemberUiModel("m1", "Ana", null, true),
        ExpenseMemberUiModel("m2", "Carlos", null, true),
        ExpenseMemberUiModel("m3", "Marta", null, false),
        ExpenseMemberUiModel("m4", "Luis", null, true),
    ),
    perPersonAmount = "15,93",
    attachmentName = "ticket-mercadona.jpg",
    attachmentSize = "1,2 MB",
    isFromScan = true,
)

@DebtshareScreenPreview
@Composable
private fun ExpenseReviewScreenPreview() {
    DebtshareTheme(darkTheme = false) {
        ExpenseReviewScreen(
            uiState = ExpenseReviewUiState.Content(previewData()),
            onBackClick = {},
            onSaveClick = {},
            onToggleMember = {},
            onFieldChanged = { _, _ -> },
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun ExpenseReviewScreenDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        ExpenseReviewScreen(
            uiState = ExpenseReviewUiState.Content(previewData()),
            onBackClick = {},
            onSaveClick = {},
            onToggleMember = {},
            onFieldChanged = { _, _ -> },
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun ExpenseReviewManualPreview() {
    DebtshareTheme(darkTheme = false) {
        ExpenseReviewScreen(
            uiState = ExpenseReviewUiState.Content(
                previewData().copy(isFromScan = false, amount = "", description = "", category = "", date = ""),
            ),
            onBackClick = {},
            onSaveClick = {},
            onToggleMember = {},
            onFieldChanged = { _, _ -> },
        )
    }
}
