package acekode.debtshare.presentation.splash

import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import debtshare.app.generated.resources.Res
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val viewModel = koinViewModel<SplashViewModel>()
    SplashContent(
        onSplashFinished = {
            viewModel.checkSession()
            onSplashFinished()
        },
    )
}

@Composable
private fun SplashContent(onSplashFinished: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background),
        contentAlignment = Alignment.Center,
    ) {
        SplashAnimation(onFinished = onSplashFinished)
    }
}

@Composable
private fun SplashAnimation(onFinished: () -> Unit) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/splash_screen_animation.json").decodeToString(),
        )
    }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
    )

    LaunchedEffect(composition, progress) {
        if (composition != null && progress == 1f) onFinished()
    }

    Image(
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress },
        ),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth(0.55f)
            .aspectRatio(1f),
    )
}

@DebtshareScreenPreview
@Composable
private fun SplashContentPreview() {
    DebtshareTheme {
        SplashContent(onSplashFinished = {})
    }
}
