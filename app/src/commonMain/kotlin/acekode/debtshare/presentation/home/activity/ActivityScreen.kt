package acekode.debtshare.presentation.home.activity

import acekode.debtshare.ui.items.DebtshareAvatar
import acekode.debtshare.ui.items.DebtshareAvatarGroup
import acekode.debtshare.ui.items.DebtshareAvatarItem
import acekode.debtshare.ui.items.DebtshareAvatarSize
import acekode.debtshare.ui.items.DebtshareBadge
import acekode.debtshare.ui.items.DebtshareBadgeSize
import acekode.debtshare.ui.items.DebtshareBadgeVariant
import acekode.debtshare.ui.items.DebtshareButton
import acekode.debtshare.ui.items.DebtshareButtonSize
import acekode.debtshare.ui.items.DebtshareButtonVariant
import acekode.debtshare.ui.items.DebtshareIcon
import acekode.debtshare.ui.items.DebtshareTabHeader
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.activity_invitation_accept
import debtshare.app.generated.resources.activity_invitation_accepted
import debtshare.app.generated.resources.activity_invitation_and_more
import debtshare.app.generated.resources.activity_invitation_decline
import debtshare.app.generated.resources.activity_invitation_invited_by
import debtshare.app.generated.resources.activity_invitation_members
import debtshare.app.generated.resources.activity_invitation_new
import debtshare.app.generated.resources.activity_mark_read
import debtshare.app.generated.resources.activity_section_recent
import debtshare.app.generated.resources.activity_tab_activity
import debtshare.app.generated.resources.activity_tab_invitations
import debtshare.app.generated.resources.activity_title
import debtshare.app.generated.resources.bag
import debtshare.app.generated.resources.check
import debtshare.app.generated.resources.check_circle
import debtshare.app.generated.resources.cup
import debtshare.app.generated.resources.house
import debtshare.app.generated.resources.search
import debtshare.app.generated.resources.star
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ActivityScreen() {
    val viewModel = koinViewModel<ActivityViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    when (uiState) {
        is ActivityUiState.Content -> ActivityContent(
            uiState = uiState as ActivityUiState.Content,
            selectedTab = selectedTab,
            onTabSelected = viewModel::onTabSelected,
            onMarkReadClick = viewModel::onMarkReadClick,
            onAcceptInvitation = viewModel::onAcceptInvitation,
            onDeclineInvitation = viewModel::onDeclineInvitation,
        )

        is ActivityUiState.Loading -> {}

        is ActivityUiState.Error -> {}
    }
}

@Composable
private fun ActivityContent(
    uiState: ActivityUiState.Content,
    selectedTab: ActivityTab,
    onTabSelected: (ActivityTab) -> Unit,
    onMarkReadClick: () -> Unit,
    onAcceptInvitation: (String) -> Unit,
    onDeclineInvitation: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background),
    ) {
        ActivityHeader(
            onMarkReadClick = onMarkReadClick,
        )

        ActivityTabBar(
            selectedTab = selectedTab,
            activityCount = uiState.activityCount,
            invitationCount = uiState.invitationCount,
            onTabSelected = onTabSelected,
        )

        when (selectedTab) {
            ActivityTab.Activity -> ActivityTabContent(
                sections = uiState.activitySections,
            )

            ActivityTab.Invitations -> InvitationsTabContent(
                invitations = uiState.invitations,
                recentInvitations = uiState.recentInvitations,
                onAcceptInvitation = onAcceptInvitation,
                onDeclineInvitation = onDeclineInvitation,
            )
        }
    }
}

@Composable
private fun ActivityHeader(
    onMarkReadClick: () -> Unit,
) {
    DebtshareTabHeader(
        title = stringResource(Res.string.activity_title),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
    ) {
        Text(
            text = stringResource(Res.string.activity_mark_read),
            style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = DebtshareColors.Brand.primary,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onMarkReadClick)
                .padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun ActivityTabBar(
    selectedTab: ActivityTab,
    activityCount: Int,
    invitationCount: Int,
    onTabSelected: (ActivityTab) -> Unit,
) {
    val borderColor = DebtshareTheme.colors.neutralTintStrong
    val strokeWidth = 1.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val stroke = strokeWidth.toPx()
                val y = size.height - stroke / 2
                drawLine(borderColor, Offset(0f, y), Offset(size.width, y), stroke)
            },
    ) {
        ActivityTabItem(
            label = stringResource(Res.string.activity_tab_activity),
            count = activityCount,
            selected = selectedTab == ActivityTab.Activity,
            onClick = { onTabSelected(ActivityTab.Activity) },
            modifier = Modifier.weight(1f),
        )
        ActivityTabItem(
            label = stringResource(Res.string.activity_tab_invitations),
            count = invitationCount,
            selected = selectedTab == ActivityTab.Invitations,
            onClick = { onTabSelected(ActivityTab.Invitations) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ActivityTabItem(
    label: String,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val indicatorColor by animateColorAsState(
        targetValue = if (selected) DebtshareColors.Brand.primary else Color.Transparent,
        animationSpec = tween(150),
    )

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = label,
                style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = if (selected) {
                    DebtshareTheme.colors.textPrimary
                } else {
                    DebtshareTheme.colors.textTertiary
                },
                maxLines = 1,
            )

            DebtshareBadge(
                text = count.toString(),
                variant = if (selected) DebtshareBadgeVariant.Brand else DebtshareBadgeVariant.Neutral,
                size = DebtshareBadgeSize.Medium,
            )
        }

        Spacer(Modifier.height(16.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(indicatorColor),
        )
    }
}

@Composable
private fun ActivityTabContent(
    sections: ImmutableList<ActivitySectionUiModel>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        sections.forEach { section ->
            Spacer(Modifier.height(16.dp))

            SectionHeader(title = section.title)

            Spacer(Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                section.items.forEach { item ->
                    ActivityItemCard(item = item)
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = DebtshareTheme.typography.labelUppercase,
        color = DebtshareTheme.colors.textTertiary,
    )
}

@Composable
private fun ActivityItemCard(item: ActivityItemUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DebtshareTheme.colors.card, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        DebtshareAvatar(
            name = item.avatarName,
            size = DebtshareAvatarSize.Large,
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.description,
                style = DebtshareTheme.typography.bodyMedium,
                color = DebtshareTheme.colors.textPrimary,
            )

            Spacer(Modifier.height(4.dp))

            ActivityItemDetail(item = item)
        }

        if (item.isUnread) {
            Spacer(Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(8.dp)
                    .background(DebtshareColors.Brand.primary, CircleShape),
            )
        }
    }
}

@Composable
private fun ActivityItemDetail(item: ActivityItemUiModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(item.actionIconContainer, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(item.actionIcon),
                contentDescription = null,
                tint = item.actionIconTint,
                modifier = Modifier.size(12.dp),
            )
        }

        Spacer(Modifier.width(6.dp))

        Text(
            text = item.detailText,
            style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = item.detailColor,
        )

        Text(
            text = "  ·  ${item.timeText}",
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textTertiary,
        )
    }
}

@Composable
private fun InvitationsTabContent(
    invitations: ImmutableList<InvitationUiModel>,
    recentInvitations: ImmutableList<RecentInvitationUiModel>,
    onAcceptInvitation: (String) -> Unit,
    onDeclineInvitation: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            invitations.forEach { invitation ->
                InvitationCard(
                    invitation = invitation,
                    onAccept = { onAcceptInvitation(invitation.id) },
                    onDecline = { onDeclineInvitation(invitation.id) },
                )
            }
        }

        if (recentInvitations.isNotEmpty()) {
            Spacer(Modifier.height(24.dp))

            SectionHeader(title = stringResource(Res.string.activity_section_recent))

            Spacer(Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                recentInvitations.forEach { recent ->
                    RecentInvitationCard(recent = recent)
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun InvitationCard(
    invitation: InvitationUiModel,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DebtshareTheme.colors.card, RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        InvitationCardHeader(invitation = invitation)

        Spacer(Modifier.height(12.dp))

        InvitationCardInviter(invitation = invitation)

        if (invitation.members.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))

            InvitationCardMembers(invitation = invitation)
        }

        Spacer(Modifier.height(16.dp))

        InvitationCardActions(onAccept = onAccept, onDecline = onDecline)
    }
}

@Composable
private fun InvitationCardHeader(invitation: InvitationUiModel) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(invitation.groupIconContainer, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            DebtshareIcon(icon = invitation.groupIcon, tint = invitation.groupIconTint)
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = invitation.groupName,
                    style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = DebtshareTheme.colors.textPrimary,
                    modifier = Modifier.weight(1f, fill = false),
                )

                if (invitation.isNew) {
                    Spacer(Modifier.width(8.dp))

                    DebtshareBadge(
                        text = stringResource(Res.string.activity_invitation_new),
                        variant = DebtshareBadgeVariant.Brand,
                        size = DebtshareBadgeSize.Small,
                        uppercase = true,
                    )
                }
            }

            Spacer(Modifier.height(2.dp))

            Text(
                text = stringResource(
                    Res.string.activity_invitation_members,
                    invitation.memberCount,
                    invitation.groupType,
                ),
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textTertiary,
            )
        }
    }
}

@Composable
private fun InvitationCardInviter(invitation: InvitationUiModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        DebtshareAvatar(
            name = invitation.inviterName,
            size = DebtshareAvatarSize.Medium,
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = stringResource(
                Res.string.activity_invitation_invited_by,
                invitation.inviterName,
                invitation.inviteTimeText,
            ),
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun InvitationCardMembers(invitation: InvitationUiModel) {
    val overflow = invitation.memberCount - invitation.members.size

    Row(verticalAlignment = Alignment.CenterVertically) {
        DebtshareAvatarGroup(
            avatars = invitation.members.map { DebtshareAvatarItem(name = it) }.toImmutableList(),
            size = DebtshareAvatarSize.Small,
            maxVisible = 3,
        )

        Spacer(Modifier.width(8.dp))

        val namesText = invitation.members.joinToString(", ")
        val displayText = if (overflow > 0) {
            stringResource(Res.string.activity_invitation_and_more, namesText, overflow)
        } else {
            namesText
        }
        Text(
            text = displayText,
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textTertiary,
        )
    }
}

@Composable
private fun InvitationCardActions(
    onAccept: () -> Unit,
    onDecline: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DebtshareButton(
            text = stringResource(Res.string.activity_invitation_decline),
            onClick = onDecline,
            variant = DebtshareButtonVariant.Secondary,
            size = DebtshareButtonSize.Large,
            modifier = Modifier.weight(1f),
        )

        DebtshareButton(
            text = stringResource(Res.string.activity_invitation_accept),
            onClick = onAccept,
            icon = Res.drawable.check,
            size = DebtshareButtonSize.Large,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun RecentInvitationCard(recent: RecentInvitationUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DebtshareTheme.colors.card, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(recent.groupIconContainer, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            DebtshareIcon(icon = recent.groupIcon, tint = recent.groupIconTint)
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = recent.groupName,
                style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = DebtshareTheme.colors.textPrimary,
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = recent.acceptedTimeText,
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textTertiary,
            )
        }

        DebtshareBadge(
            text = stringResource(Res.string.activity_invitation_accepted),
            variant = DebtshareBadgeVariant.Success,
            size = DebtshareBadgeSize.Medium,
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun ActivityContentActivityTabPreview() {
    DebtshareTheme(darkTheme = false) {
        ActivityContent(
            uiState = previewContent(),
            selectedTab = ActivityTab.Activity,
            onTabSelected = {},
            onMarkReadClick = {},
            onAcceptInvitation = {},
            onDeclineInvitation = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun ActivityContentInvitationsTabPreview() {
    DebtshareTheme(darkTheme = false) {
        ActivityContent(
            uiState = previewContent(),
            selectedTab = ActivityTab.Invitations,
            onTabSelected = {},
            onMarkReadClick = {},
            onAcceptInvitation = {},
            onDeclineInvitation = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun ActivityContentDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        ActivityContent(
            uiState = previewContent(),
            selectedTab = ActivityTab.Activity,
            onTabSelected = {},
            onMarkReadClick = {},
            onAcceptInvitation = {},
            onDeclineInvitation = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun ActivityContentInvitationsDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        ActivityContent(
            uiState = previewContent(),
            selectedTab = ActivityTab.Invitations,
            onTabSelected = {},
            onMarkReadClick = {},
            onAcceptInvitation = {},
            onDeclineInvitation = {},
        )
    }
}

private fun previewContent() = ActivityUiState.Content(
    activityCount = 3,
    invitationCount = 2,
    unreadCount = 5,
    activitySections = previewActivitySections(),
    invitations = previewInvitations(),
    recentInvitations = persistentListOf(
        RecentInvitationUiModel(
            id = "recent1",
            groupName = "Piso Castellana 43",
            groupIcon = Res.drawable.house,
            groupIconTint = DebtshareColors.Brand.primary,
            groupIconContainer = DebtshareColors.Brand.primaryTint,
            acceptedTimeText = "aceptaste hace 3 días",
        ),
    ),
)

private fun previewActivitySections() = persistentListOf(
    ActivitySectionUiModel(
        title = "HOY",
        items = persistentListOf(
            ActivityItemUiModel(
                id = "1",
                avatarName = "Carlos Mena",
                description = "Carlos añadió Compra Mercadona en Piso Castellana 43",
                actionIcon = Res.drawable.bag,
                actionIconTint = DebtshareColors.Accent.mustardDark,
                actionIconContainer = DebtshareColors.Accent.mustardTint,
                detailText = "-11,95 €",
                detailColor = DebtshareColors.Semantic.error,
                timeText = "hace 12 min",
                isUnread = true,
            ),
            ActivityItemUiModel(
                id = "2",
                avatarName = "Luis García",
                description = "Luis confirmó tu pago de 12,00 €",
                actionIcon = Res.drawable.check_circle,
                actionIconTint = DebtshareColors.Semantic.success,
                actionIconContainer = DebtshareColors.Semantic.successTintSoft,
                detailText = "Liquidado ✓",
                detailColor = DebtshareColors.Semantic.success,
                timeText = "hace 1 h",
                isUnread = false,
            ),
        ),
    ),
    ActivitySectionUiModel(
        title = "AYER",
        items = persistentListOf(
            ActivityItemUiModel(
                id = "3",
                avatarName = "Carlos Mena",
                description = "Carlos escaneó un ticket de Netflix",
                actionIcon = Res.drawable.search,
                actionIconTint = DebtshareColors.Brand.primary,
                actionIconContainer = DebtshareColors.Brand.primaryTint,
                detailText = "-3,32 €",
                detailColor = DebtshareColors.Semantic.error,
                timeText = "ayer 21:14",
                isUnread = false,
            ),
        ),
    ),
)

private fun previewInvitations() = persistentListOf(
    InvitationUiModel(
        id = "inv1",
        groupName = "Viaje Roma 2026",
        groupIcon = Res.drawable.star,
        groupIconTint = DebtshareColors.Brand.primary,
        groupIconContainer = DebtshareColors.Brand.primaryTint,
        memberCount = 6,
        groupType = "viaje",
        inviterName = "Elena",
        inviteTimeText = "hace 30 min",
        members = persistentListOf("Elena", "Nuria", "Pablo"),
        isNew = true,
    ),
    InvitationUiModel(
        id = "inv2",
        groupName = "Cena cumpleaños Pablo",
        groupIcon = Res.drawable.cup,
        groupIconTint = DebtshareColors.Accent.plum,
        groupIconContainer = DebtshareColors.Accent.plumTint,
        memberCount = 8,
        groupType = "cena",
        inviterName = "Pablo",
        inviteTimeText = "ayer",
        members = persistentListOf(),
        isNew = false,
    ),
)
