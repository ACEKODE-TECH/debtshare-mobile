package acekode.debtshare.presentation.home.groups.addexpense

import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.logo
import debtshare.app.generated.resources.scan_analysis_hint
import debtshare.app.generated.resources.scan_analysis_step_1
import debtshare.app.generated.resources.scan_analysis_step_2
import debtshare.app.generated.resources.scan_analysis_step_3
import debtshare.app.generated.resources.scan_analysis_step_4
import debtshare.app.generated.resources.scan_analysis_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.floor

private const val BAR_COUNT = 4
private const val CYCLE_DURATION_MS = 3000
private val analysisSteps = listOf(
    Res.string.scan_analysis_step_1,
    Res.string.scan_analysis_step_2,
    Res.string.scan_analysis_step_3,
    Res.string.scan_analysis_step_4,
)

@Composable
internal fun ScanAnalysisView() {
    val transition = rememberInfiniteTransition()
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = BAR_COUNT.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(CYCLE_DURATION_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
    )
    val filledBars = floor(progress).toInt()
    val stepIndex = (filledBars * analysisSteps.size / BAR_COUNT)
        .coerceIn(0, analysisSteps.lastIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp)),
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = stringResource(Res.string.scan_analysis_title),
            style = DebtshareTheme.typography.displayMedium,
            color = DebtshareTheme.colors.textPrimary,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(analysisSteps[stepIndex]),
            style = DebtshareTheme.typography.bodyMedium,
            color = DebtshareColors.Brand.primary,
        )

        Spacer(Modifier.height(24.dp))

        ProgressBars(filledBars = filledBars)

        Spacer(Modifier.weight(1f))

        Text(
            text = stringResource(Res.string.scan_analysis_hint),
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textTertiary,
        )

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun ProgressBars(filledBars: Int) {
    Row(
        modifier = Modifier.padding(horizontal = 80.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(BAR_COUNT) { index ->
            val color = if (index < filledBars) {
                DebtshareColors.Brand.primary
            } else {
                DebtshareTheme.colors.neutralTint
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color),
            )
        }
    }
}

@DebtshareScreenPreview
@Composable
private fun ScanAnalysisPreview() {
    DebtshareTheme(darkTheme = true) {
        ScanAnalysisView()
    }
}

@DebtshareScreenPreview
@Composable
private fun ScanAnalysisLightPreview() {
    DebtshareTheme(darkTheme = false) {
        ScanAnalysisView()
    }
}
