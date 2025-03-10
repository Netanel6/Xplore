package com.netanel.xplore.quiz.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.netanel.xplore.ui.theme.OnPrimary

@Composable
fun PointsAnimationScreen(
    points: Int,
    isCorrect: Boolean,
    correctAnswer: String,
    explanation: String,
    onAnimationEnd: () -> Unit
) {
    val animationFile = if (isCorrect) "correct.json" else "wrong.json"
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(animationFile))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    // Trigger the animation end callback when the animation is completed
    LaunchedEffect(progress) {
        if (progress == 1f) onAnimationEnd()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display the Lottie animation
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            val answerText = if (isCorrect) "+$points נקודות" else " התשובה היא:\n $correctAnswer"
            // Show feedback text based on correctness
            Text(
                text = answerText,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isCorrect) OnPrimary else MaterialTheme.colorScheme.error
                ),
                textAlign = TextAlign.Center
            )

            // Show explanation only if the answer is correct
            if (isCorrect) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = explanation,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}