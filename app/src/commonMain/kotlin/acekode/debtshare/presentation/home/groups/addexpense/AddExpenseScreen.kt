package acekode.debtshare.presentation.home.groups.addexpense

import acekode.debtshare.ui.items.DebtshareButton
import acekode.debtshare.ui.items.DebtshareButtonSize
import acekode.debtshare.ui.items.DebtshareButtonVariant
import acekode.debtshare.ui.items.DebtshareIcon
import acekode.debtshare.ui.items.DebtshareIconSize
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import acekode.debtshare.utils.rememberCameraLauncher
import acekode.debtshare.utils.rememberFilePickerLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.add_expense_autocomplete
import debtshare.app.generated.resources.add_expense_choose_file
import debtshare.app.generated.resources.add_expense_how_it_works
import debtshare.app.generated.resources.add_expense_manual
import debtshare.app.generated.resources.add_expense_or_manual
import debtshare.app.generated.resources.add_expense_scan_description
import debtshare.app.generated.resources.add_expense_scan_title
import debtshare.app.generated.resources.add_expense_step_1
import debtshare.app.generated.resources.add_expense_step_2
import debtshare.app.generated.resources.add_expense_step_3
import debtshare.app.generated.resources.add_expense_take_photo
import debtshare.app.generated.resources.add_expense_title
import debtshare.app.generated.resources.arrow_left
import debtshare.app.generated.resources.camera
import debtshare.app.generated.resources.file
import debtshare.app.generated.resources.house
import debtshare.app.generated.resources.plus
import debtshare.app.generated.resources.scan
import debtshare.app.generated.resources.star
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddExpenseScreen(
    groupName: String,
    onBackClick: () -> Unit,
    onImageCaptured: (ByteArray?) -> Unit,
    onManualEntryClick: () -> Unit,
) {
    val launchCamera = rememberCameraLauncher(onResult = onImageCaptured)
    val launchFilePicker = rememberFilePickerLauncher(onResult = onImageCaptured)

    AddExpenseContent(
        groupName = groupName,
        onBackClick = onBackClick,
        onTakePhotoClick = launchCamera,
        onChooseFileClick = launchFilePicker,
        onManualEntryClick = onManualEntryClick,
    )
}

@Composable
private fun AddExpenseContent(
    groupName: String,
    onBackClick: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onChooseFileClick: () -> Unit,
    onManualEntryClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background)
            .verticalScroll(rememberScrollState()),
    ) {
        AddExpenseTopBar(onBackClick = onBackClick)

        Spacer(Modifier.height(8.dp))

        GroupNameRow(groupName = groupName)

        Spacer(Modifier.height(16.dp))

        ScanTicketCard(
            onTakePhotoClick = onTakePhotoClick,
            onChooseFileClick = onChooseFileClick,
        )

        Spacer(Modifier.height(24.dp))

        ManualDivider()

        Spacer(Modifier.height(16.dp))

        ManualEntryButton(onClick = onManualEntryClick)

        Spacer(Modifier.height(24.dp))

        HowItWorksSection()

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun AddExpenseTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DebtshareIcon(icon = Res.drawable.arrow_left, onClick = onBackClick)

        Text(
            text = stringResource(Res.string.add_expense_title),
            style = DebtshareTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            color = DebtshareTheme.colors.textPrimary,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.size(24.dp))
    }
}

@Composable
private fun GroupNameRow(groupName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        DebtshareIcon(
            icon = Res.drawable.house,
            tint = DebtshareTheme.colors.textPrimary,
            size = DebtshareIconSize.Medium,
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = groupName,
            style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = DebtshareTheme.colors.textPrimary,
        )
    }
}

@Composable
private fun ScanTicketCard(
    onTakePhotoClick: () -> Unit,
    onChooseFileClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DebtshareColors.Brand.primaryTint)
            .border(1.dp, DebtshareColors.Brand.primary.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DebtshareIcon(
            icon = Res.drawable.scan,
            tint = DebtshareColors.Brand.primary,
            size = DebtshareIconSize.Large,
        )

        Spacer(Modifier.height(12.dp))

        AutocompleteBadge()

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.add_expense_scan_title),
            style = DebtshareTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            color = DebtshareColors.Neutral.n900,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = stringResource(Res.string.add_expense_scan_description),
            style = DebtshareTheme.typography.bodyMedium,
            color = DebtshareColors.Neutral.n700,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DebtshareButton(
                text = stringResource(Res.string.add_expense_take_photo),
                onClick = onTakePhotoClick,
                variant = DebtshareButtonVariant.Primary,
                icon = Res.drawable.camera,
                size = DebtshareButtonSize.Large,
                modifier = Modifier.weight(1f),
            )
            DebtshareButton(
                text = stringResource(Res.string.add_expense_choose_file),
                onClick = onChooseFileClick,
                variant = DebtshareButtonVariant.Secondary,
                icon = Res.drawable.file,
                size = DebtshareButtonSize.Large,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun AutocompleteBadge() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(DebtshareColors.Brand.primary)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        DebtshareIcon(
            icon = Res.drawable.star,
            tint = DebtshareColors.Neutral.n0,
            size = DebtshareIconSize.Small,
        )
        Text(
            text = stringResource(Res.string.add_expense_autocomplete),
            style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = DebtshareColors.Neutral.n0,
        )
    }
}

@Composable
private fun ManualDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = DebtshareTheme.colors.border,
        )
        Text(
            text = stringResource(Res.string.add_expense_or_manual),
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textTertiary,
            modifier = Modifier.padding(horizontal = 12.dp),
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = DebtshareTheme.colors.border,
        )
    }
}

@Composable
private fun ManualEntryButton(onClick: () -> Unit) {
    DebtshareButton(
        text = stringResource(Res.string.add_expense_manual),
        onClick = onClick,
        variant = DebtshareButtonVariant.Secondary,
        icon = Res.drawable.plus,
        size = DebtshareButtonSize.Large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )
}

@Composable
private fun HowItWorksSection() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DebtshareTheme.colors.card)
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(Res.string.add_expense_how_it_works),
            style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = DebtshareTheme.colors.textPrimary,
        )

        Spacer(Modifier.height(12.dp))

        val steps = listOf(
            Res.string.add_expense_step_1,
            Res.string.add_expense_step_2,
            Res.string.add_expense_step_3,
        )
        steps.forEachIndexed { index, step ->
            StepRow(number = index + 1, text = stringResource(step))
            if (index < steps.lastIndex) Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun StepRow(number: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(DebtshareColors.Brand.primaryTint),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = number.toString(),
                style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = DebtshareColors.Brand.primary,
            )
        }
        Text(
            text = text,
            style = DebtshareTheme.typography.bodyMedium,
            color = DebtshareTheme.colors.textSecondary,
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun AddExpenseScreenPreview() {
    DebtshareTheme(darkTheme = false) {
        AddExpenseContent(
            groupName = "Piso Castellana 43",
            onBackClick = {},
            onTakePhotoClick = {},
            onChooseFileClick = {},
            onManualEntryClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun AddExpenseScreenDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        AddExpenseContent(
            groupName = "Piso Castellana 43",
            onBackClick = {},
            onTakePhotoClick = {},
            onChooseFileClick = {},
            onManualEntryClick = {},
        )
    }
}
