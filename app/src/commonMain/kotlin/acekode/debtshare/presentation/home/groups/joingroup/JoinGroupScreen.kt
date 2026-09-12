package acekode.debtshare.presentation.home.groups.joingroup

import acekode.debtshare.ui.items.DebtshareAvatarGroup
import acekode.debtshare.ui.items.DebtshareAvatarItem
import acekode.debtshare.ui.items.DebtshareAvatarSize
import acekode.debtshare.ui.items.DebtshareButton
import acekode.debtshare.ui.items.DebtshareButtonSize
import acekode.debtshare.ui.items.DebtshareButtonVariant
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.app_name
import debtshare.app.generated.resources.calendar
import debtshare.app.generated.resources.check
import debtshare.app.generated.resources.clock
import debtshare.app.generated.resources.close
import debtshare.app.generated.resources.dollar
import debtshare.app.generated.resources.house
import debtshare.app.generated.resources.join_group_active
import debtshare.app.generated.resources.join_group_decline
import debtshare.app.generated.resources.join_group_details
import debtshare.app.generated.resources.join_group_details_expired
import debtshare.app.generated.resources.join_group_details_generated_by
import debtshare.app.generated.resources.join_group_details_group
import debtshare.app.generated.resources.join_group_disclaimer
import debtshare.app.generated.resources.join_group_expenses
import debtshare.app.generated.resources.join_group_expired_description
import debtshare.app.generated.resources.join_group_expired_label
import debtshare.app.generated.resources.join_group_expired_title
import debtshare.app.generated.resources.join_group_go_home
import debtshare.app.generated.resources.join_group_inactive
import debtshare.app.generated.resources.join_group_invited_by
import debtshare.app.generated.resources.join_group_join
import debtshare.app.generated.resources.join_group_label
import debtshare.app.generated.resources.join_group_members
import debtshare.app.generated.resources.logo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun JoinGroupScreen(
    uiState: JoinGroupUiState,
    onJoinClick: () -> Unit,
    onDeclineClick: () -> Unit,
    onGoHomeClick: () -> Unit,
) {
    when (uiState) {
        is JoinGroupUiState.Loading -> {}

        is JoinGroupUiState.Valid -> ValidInvitationContent(
            uiState = uiState,
            onJoinClick = onJoinClick,
            onDeclineClick = onDeclineClick,
        )

        is JoinGroupUiState.Expired -> ExpiredInvitationContent(
            uiState = uiState,
            onGoHomeClick = onGoHomeClick,
        )
    }
}

@Composable
private fun ValidInvitationContent(
    uiState: JoinGroupUiState.Valid,
    onJoinClick: () -> Unit,
    onDeclineClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JoinGroupTopBar(inviteCode = uiState.inviteCode)

        Spacer(Modifier.weight(1f))

        InvitationCard(
            headerColor = DebtshareColors.Brand.primaryTint,
            heroContent = {
                Icon(
                    painter = painterResource(Res.drawable.house),
                    contentDescription = null,
                    tint = DebtshareColors.Brand.primary,
                    modifier = Modifier.size(48.dp),
                )
            },
        ) {
            GroupInfoSection(uiState = uiState)
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(32.dp))

        JoinFooter(onJoinClick = onJoinClick, onDeclineClick = onDeclineClick)
    }
}

@Composable
private fun ExpiredInvitationContent(
    uiState: JoinGroupUiState.Expired,
    onGoHomeClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JoinGroupTopBar(inviteCode = uiState.inviteCode)

        Spacer(Modifier.weight(1f))

        InvitationCard(
            headerColor = DebtshareColors.Accent.mustardTint,
            heroContent = {
                Box {
                    Icon(
                        painter = painterResource(Res.drawable.clock),
                        contentDescription = null,
                        tint = DebtshareColors.Accent.mustardDark,
                        modifier = Modifier.size(48.dp),
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(20.dp)
                            .background(DebtshareColors.Semantic.error, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.close),
                            contentDescription = null,
                            tint = DebtshareColors.Neutral.n0,
                            modifier = Modifier.size(12.dp),
                        )
                    }
                }
            },
        ) {
            ExpiredInfoSection(uiState = uiState)
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(32.dp))

        DebtshareButton(
            text = stringResource(Res.string.join_group_go_home),
            onClick = onGoHomeClick,
            variant = DebtshareButtonVariant.Secondary,
            size = DebtshareButtonSize.Large,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun GroupInfoSection(uiState: JoinGroupUiState.Valid) {
    Text(
        text = stringResource(Res.string.join_group_label),
        style = DebtshareTheme.typography.labelUppercase,
        color = DebtshareColors.Brand.primary,
    )

    Spacer(Modifier.height(8.dp))

    Text(
        text = uiState.groupName,
        style = DebtshareTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
        color = DebtshareTheme.colors.textPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(4.dp))

    Text(
        text = uiState.groupDescription,
        style = DebtshareTheme.typography.bodyMedium,
        color = DebtshareTheme.colors.textSecondary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(16.dp))

    InviterRow(inviterName = uiState.inviterName)

    Spacer(Modifier.height(20.dp))

    MembersRow(members = uiState.members, membersCount = uiState.membersCount)

    Spacer(Modifier.height(20.dp))

    GroupTagsRow(
        currency = uiState.tags.currency,
        expenseCount = uiState.tags.expenseCount,
        isActive = uiState.tags.isActive,
    )
}

@Composable
private fun JoinFooter(
    onJoinClick: () -> Unit,
    onDeclineClick: () -> Unit,
) {
    DebtshareButton(
        text = stringResource(Res.string.join_group_join),
        onClick = onJoinClick,
        icon = Res.drawable.check,
        size = DebtshareButtonSize.Large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    )

    Spacer(Modifier.height(12.dp))

    DebtshareButton(
        text = stringResource(Res.string.join_group_decline),
        onClick = onDeclineClick,
        variant = DebtshareButtonVariant.Secondary,
        size = DebtshareButtonSize.Large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    )

    Spacer(Modifier.height(8.dp))

    Text(
        text = stringResource(Res.string.join_group_disclaimer),
        style = DebtshareTheme.typography.bodySmall,
        color = DebtshareTheme.colors.textTertiary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 32.dp),
    )

    Spacer(Modifier.height(24.dp))
}

@Composable
private fun ExpiredInfoSection(uiState: JoinGroupUiState.Expired) {
    Text(
        text = stringResource(Res.string.join_group_expired_label),
        style = DebtshareTheme.typography.labelUppercase,
        color = DebtshareColors.Accent.mustardDark,
    )

    Spacer(Modifier.height(8.dp))

    Text(
        text = stringResource(Res.string.join_group_expired_title),
        style = DebtshareTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
        color = DebtshareTheme.colors.textPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(8.dp))

    Text(
        text = stringResource(Res.string.join_group_expired_description),
        style = DebtshareTheme.typography.bodyMedium,
        color = DebtshareTheme.colors.textSecondary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(24.dp))

    DetailsCard(
        groupName = uiState.groupName,
        generatedBy = uiState.generatedBy,
        expiredAgo = uiState.expiredAgo,
    )
}

@Composable
private fun JoinGroupTopBar(inviteCode: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)),
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = stringResource(Res.string.app_name),
            style = DebtshareTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            color = DebtshareTheme.colors.textPrimary,
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = inviteCode,
            style = DebtshareTheme.typography.bodyLarge,
            color = DebtshareTheme.colors.textTertiary,
        )
    }
}

@Composable
private fun InvitationCard(
    headerColor: Color,
    heroContent: @Composable () -> Unit,
    body: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, DebtshareTheme.colors.border, RoundedCornerShape(20.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(headerColor),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(DebtshareColors.Neutral.n0, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center,
            ) {
                heroContent()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DebtshareTheme.colors.card)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            body()
        }
    }
}

@Composable
private fun InviterRow(inviterName: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(DebtshareColors.Accent.violet, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = inviterName.take(1).uppercase(),
                style = DebtshareTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                ),
                color = DebtshareColors.Neutral.n0,
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(Res.string.join_group_invited_by, inviterName),
            style = DebtshareTheme.typography.bodyMedium,
            color = DebtshareTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun MembersRow(
    members: ImmutableList<JoinGroupMemberUiModel>,
    membersCount: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (members.isNotEmpty()) {
            DebtshareAvatarGroup(
                avatars = members.map { DebtshareAvatarItem(name = it.name) }.toPersistentList(),
                size = DebtshareAvatarSize.Small,
                maxVisible = 4,
            )
            Spacer(Modifier.width(10.dp))
        }
        Column {
            Text(
                text = stringResource(Res.string.join_group_members, membersCount),
                style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = DebtshareTheme.colors.textPrimary,
            )
            if (members.isNotEmpty()) {
                Text(
                    text = members.joinToString(", ") { it.name.substringBefore(" ") },
                    style = DebtshareTheme.typography.bodySmall,
                    color = DebtshareTheme.colors.textSecondary,
                )
            }
        }
    }
}

@Composable
private fun GroupTagsRow(
    currency: String,
    expenseCount: Int,
    isActive: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        GroupTag(icon = Res.drawable.dollar, text = currency)
        GroupTag(
            icon = Res.drawable.calendar,
            text = stringResource(Res.string.join_group_expenses, expenseCount),
        )
        if (isActive) {
            GroupTag(
                icon = Res.drawable.clock,
                text = stringResource(Res.string.join_group_active),
            )
        } else {
            GroupTag(
                icon = Res.drawable.clock,
                text = stringResource(Res.string.join_group_inactive),
                tintColor = DebtshareColors.Accent.mustardDark,
            )
        }
    }
}

@Composable
private fun GroupTag(
    icon: DrawableResource,
    text: String,
    tintColor: Color = DebtshareTheme.colors.textTertiary,
) {
    Row(
        modifier = Modifier
            .border(1.dp, DebtshareTheme.colors.border, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tintColor,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = text,
            style = DebtshareTheme.typography.bodySmall,
            color = tintColor,
        )
    }
}

@Composable
private fun DetailsCard(
    groupName: String,
    generatedBy: String,
    expiredAgo: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DebtshareTheme.colors.border, RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(Res.string.join_group_details),
            style = DebtshareTheme.typography.labelUppercase,
            color = DebtshareTheme.colors.textTertiary,
        )

        Spacer(Modifier.height(12.dp))

        DetailRow(
            label = stringResource(Res.string.join_group_details_group),
            value = groupName,
        )
        Spacer(Modifier.height(8.dp))
        DetailRow(
            label = stringResource(Res.string.join_group_details_generated_by),
            value = generatedBy,
        )
        Spacer(Modifier.height(8.dp))
        DetailRow(
            label = stringResource(Res.string.join_group_details_expired),
            value = expiredAgo,
            valueColor = DebtshareColors.Semantic.error,
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = DebtshareTheme.colors.textPrimary,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = DebtshareTheme.typography.bodyMedium,
            color = DebtshareTheme.colors.textSecondary,
        )
        Text(
            text = value,
            style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = valueColor,
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun JoinGroupValidPreview() {
    DebtshareTheme(darkTheme = false) {
        JoinGroupScreen(
            uiState = previewValid(),
            onJoinClick = {},
            onDeclineClick = {},
            onGoHomeClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun JoinGroupExpiredPreview() {
    DebtshareTheme(darkTheme = false) {
        JoinGroupScreen(
            uiState = previewExpired(),
            onJoinClick = {},
            onDeclineClick = {},
            onGoHomeClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun JoinGroupValidDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        JoinGroupScreen(
            uiState = previewValid(),
            onJoinClick = {},
            onDeclineClick = {},
            onGoHomeClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun JoinGroupExpiredDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        JoinGroupScreen(
            uiState = previewExpired(),
            onJoinClick = {},
            onDeclineClick = {},
            onGoHomeClick = {},
        )
    }
}

private fun previewValid() = JoinGroupUiState.Valid(
    inviteCode = "j/pisocast-a7f2k9",
    groupName = "Piso Castellana 43",
    groupDescription = "Gastos compartidos del piso · desde marzo 2025",
    inviterName = "Ana G.",
    members = persistentListOf(
        JoinGroupMemberUiModel("Ana García", DebtshareColors.Accent.violet),
        JoinGroupMemberUiModel("Carlos Ruiz", DebtshareColors.Brand.primary),
        JoinGroupMemberUiModel("Marta López", DebtshareColors.Accent.plum),
        JoinGroupMemberUiModel("Luis Prat", DebtshareColors.Semantic.success),
    ),
    tags = GroupTagsUiModel(currency = "EUR", expenseCount = 32, isActive = true),
)

private fun previewExpired() = JoinGroupUiState.Expired(
    inviteCode = "j/pisocast-a7f2k9",
    groupName = "Piso Castellana 43",
    generatedBy = "Ana G.",
    expiredAgo = "hace 2 días",
)
