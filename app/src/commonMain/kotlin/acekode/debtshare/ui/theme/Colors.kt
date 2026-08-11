package acekode.debtshare.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

object DebtshareColors {

    object Brand {
        val primary = Color(0xFF3B6EF6)
        val primaryLight = Color(0xFF5B8AF6)
        val primaryTint = Color(0xFFEFF6FF)
        val secondary = Color(0xFF4F46E5)
    }

    object Accent {
        val mustard = Color(0xFFFBBF24)
        val mustardDark = Color(0xFFD97706)
        val mustardTint = Color(0xFFFEF3C7)
        val plum = Color(0xFFBE185D)
        val plumBright = Color(0xFFEC4899)
        val plumTint = Color(0xFFFCE7F3)
        val violet = Color(0xFF7C3AED)
    }

    object Neutral {
        val n0 = Color(0xFFFFFFFF)
        val n50 = Color(0xFFF5F6FA)
        val n100 = Color(0xFFEEF0F4)
        val n200 = Color(0xFFE5E7EB)
        val n300 = Color(0xFFD1D5DB)
        val n400 = Color(0xFF9CA3AF)
        val n500 = Color(0xFF6B7280)
        val n600 = Color(0xFF4B5563)
        val n700 = Color(0xFF374151)
        val n800 = Color(0xFF282D3A)
        val n850 = Color(0xFF1E2230)
        val n900 = Color(0xFF12151E)
        val n950 = Color(0xFF0C0E14)
    }

    object Semantic {
        val success = Color(0xFF059669)
        val successTintSoft = Color(0xFFF0FDF4)
        val error = Color(0xFFDC2626)
        val errorBright = Color(0xFFEF4444)
        val errorLight = Color(0xFFF87171)
        val errorTint = Color(0xFFFEE2E2)
        val errorTintSoft = Color(0xFFFEF2F2)
    }

    object External {
        val googleBlue = Color(0xFF4285F4)
        val googleRed = Color(0xFFEA4335)
        val googleYellow = Color(0xFFFBBC05)
        val googleGreen = Color(0xFF34A853)
        val whatsapp = Color(0xFF25D366)
    }

    val avatarTints = listOf(
        Brand.primary,
        Brand.secondary,
        Accent.violet,
        Accent.plum,
        Accent.plumBright,
        Accent.mustard,
        Semantic.success,
        Semantic.errorBright,
    )
}

@Immutable
data class DebtshareColorScheme(
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textMuted: Color,
    val background: Color,
    val card: Color,
    val neutralTint: Color,
    val neutralTintStrong: Color,
    val border: Color,
    val skeletonHighlight: Color,
    val disabledDestructiveContainer: Color,
    val googleButtonBackground: Color,
    val googleButtonContent: Color,
    val googleButtonBorder: Color,
)

internal val LightDebtshareColorScheme = DebtshareColorScheme(
    textPrimary = DebtshareColors.Neutral.n900,
    textSecondary = DebtshareColors.Neutral.n700,
    textTertiary = DebtshareColors.Neutral.n500,
    textMuted = DebtshareColors.Neutral.n400,
    background = DebtshareColors.Neutral.n50,
    card = DebtshareColors.Neutral.n0,
    neutralTint = DebtshareColors.Neutral.n100,
    neutralTintStrong = DebtshareColors.Neutral.n100,
    border = DebtshareColors.Neutral.n200,
    skeletonHighlight = DebtshareColors.Neutral.n50,
    disabledDestructiveContainer = DebtshareColors.Semantic.errorTint,
    googleButtonBackground = Color(0xFFFFFFFF),
    googleButtonContent = Color(0xFF1F1F1F),
    googleButtonBorder = Color(0xFF747775),
)

internal val DarkDebtshareColorScheme = DebtshareColorScheme(
    textPrimary = DebtshareColors.Neutral.n100,
    textSecondary = DebtshareColors.Neutral.n300,
    textTertiary = DebtshareColors.Neutral.n400,
    textMuted = DebtshareColors.Neutral.n500,
    background = DebtshareColors.Neutral.n900,
    card = DebtshareColors.Neutral.n850,
    neutralTint = DebtshareColors.Neutral.n850,
    neutralTintStrong = DebtshareColors.Neutral.n800,
    border = DebtshareColors.Neutral.n800,
    skeletonHighlight = DebtshareColors.Neutral.n800,
    disabledDestructiveContainer = DebtshareColors.Semantic.error.copy(alpha = 0.5f),
    googleButtonBackground = Color(0xFF131314),
    googleButtonContent = Color(0xFFE3E3E3),
    googleButtonBorder = Color.Transparent,
)
