package acekode.debtshare.presentation.home.profile

import acekode.debtshare.ui.theme.DebtshareColors
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.bag
import debtshare.app.generated.resources.car
import debtshare.app.generated.resources.cup
import debtshare.app.generated.resources.dots
import debtshare.app.generated.resources.house
import debtshare.app.generated.resources.star
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.DrawableResource
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel : ViewModel() {

    val uiState: StateFlow<ProfileUiState>
        field = MutableStateFlow<ProfileUiState>(mockContent())
}

private fun mockContent(): ProfileUiState.Content = ProfileUiState.Content(
    user = ProfileUser(
        name = "Ana García",
        alias = "anag",
        groupCount = 5,
        expenseCount = 32,
    ),
    balance = ProfileBalance(
        total = "+48,00 €",
        isPositive = true,
        owed = "+72,50 €",
        owing = "−24,50 €",
        thisMonth = "312 €",
    ),
    analytics = ProfileAnalytics(
        groupCount = 5,
        totalSpent = "1.284 €",
        spentDelta = "+8% vs 90 días previos",
        isDeltaPositive = true,
        averageTicket = "18,60 €",
        expenseCount = 69,
    ),
    categories = mockCategories(),
    groupComparisons = mockGroupComparisons(),
    account = ProfileAccount(paymentMethod = "Bizum"),
)

private fun mockCategories() = persistentListOf(
    mockCategory("Compra", Res.drawable.bag, DebtshareColors.Accent.mustardDark, "462 €", 36),
    mockCategory("Restaurantes", Res.drawable.cup, DebtshareColors.Accent.plum, "298 €", 23),
    mockCategory("Casa", Res.drawable.house, DebtshareColors.Brand.primary, "214 €", 17),
    mockCategory("Transporte", Res.drawable.car, DebtshareColors.Semantic.success, "142 €", 11),
    mockCategory("Ocio", Res.drawable.star, DebtshareColors.Brand.secondary, "98 €", 8),
    mockCategory("Otros", Res.drawable.dots, DebtshareColors.Neutral.n400, "70 €", 5),
)

private fun mockCategory(
    name: String,
    icon: DrawableResource,
    color: Color,
    amount: String,
    percentage: Int,
) = ProfileCategory(
    name = name,
    icon = icon,
    iconTint = color,
    barColor = color,
    amount = amount,
    percentage = percentage,
    barProgress = percentage / 100f,
)

private fun mockGroupComparisons() = persistentListOf(
    ProfileGroupComparison(
        id = "1",
        name = "Piso Castellana 43",
        icon = Res.drawable.house,
        iconTint = DebtshareColors.Brand.primary,
        barColor = DebtshareColors.Brand.primary,
        memberCount = 4,
        expenseCount = 18,
        totalAmount = "548 €",
        balance = ProfileGroupBalance.Positive("+48,00 €"),
        barProgress = 1f,
    ),
    ProfileGroupComparison(
        id = "2",
        name = "Viaje Roma 2026",
        icon = Res.drawable.star,
        iconTint = DebtshareColors.Accent.mustardDark,
        barColor = DebtshareColors.Accent.mustardDark,
        memberCount = 6,
        expenseCount = 12,
        totalAmount = "384 €",
        balance = ProfileGroupBalance.Negative("−32,00 €"),
        barProgress = 0.7f,
    ),
    ProfileGroupComparison(
        id = "3",
        name = "Cena cumpleaños Pablo",
        icon = Res.drawable.cup,
        iconTint = DebtshareColors.Accent.plum,
        barColor = DebtshareColors.Accent.plum,
        memberCount = 8,
        expenseCount = 3,
        totalAmount = "198 €",
        balance = ProfileGroupBalance.Positive("+24,50 €"),
        barProgress = 0.36f,
    ),
    ProfileGroupComparison(
        id = "4",
        name = "Coworking Julio",
        icon = Res.drawable.house,
        iconTint = DebtshareColors.Semantic.success,
        barColor = DebtshareColors.Semantic.success,
        memberCount = 3,
        expenseCount = 5,
        totalAmount = "108 €",
        balance = ProfileGroupBalance.Settled,
        barProgress = 0.2f,
    ),
    ProfileGroupComparison(
        id = "5",
        name = "Finde Sierra",
        icon = Res.drawable.star,
        iconTint = DebtshareColors.Brand.secondary,
        barColor = DebtshareColors.Brand.secondary,
        memberCount = 5,
        expenseCount = 4,
        totalAmount = "46 €",
        balance = ProfileGroupBalance.Settled,
        barProgress = 0.08f,
    ),
)
