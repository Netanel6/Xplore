package com.netanel.xplore.quiz.ui.composables.finish

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.netanel.xplore.R
import kotlinx.coroutines.delay

@Composable
fun QuizFinishedAnimation(onAnimationEnd: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("times_up.json"))
    val progress by animateLottieCompositionAsState(composition, iterations = 1)

    // Scale Animation for Text (Alternative, if you don't use Lottie)
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        delay(3000L) // Keep visible for 3 seconds
        onAnimationEnd()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = true, // Always visible, we control the content's animation
            enter = fadeIn(animationSpec = tween(800)),
            exit = fadeOut(animationSpec = tween(800))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) { // Center text
                if (composition != null) { // Check if Lottie composition is loaded
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.fillMaxSize(0.7f) // Adjust size as needed
                    )
                } else {
                    // Fallback text (if Lottie fails or as an alternative)
                    Text(
                        text = stringResource(R.string.times_up),
                        style = MaterialTheme.typography.displayLarge, // Use a larger style
                        color = MaterialTheme.colorScheme.error, // Use error color for impact
                        fontSize = 48.sp, // Explicitly set a large font size
                        modifier = Modifier.scale(scale.value) // Apply the scale animation
                    )
                }
            }
        }
    }
}