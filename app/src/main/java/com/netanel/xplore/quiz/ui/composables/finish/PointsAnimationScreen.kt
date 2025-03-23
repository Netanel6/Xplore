package com.netanel.xplore.quiz.ui.composables.finish

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.netanel.xplore.R
import com.netanel.xplore.quiz.ui.QuizResult
import com.netanel.xplore.ui.AnimatedComposable
import com.netanel.xplore.ui.theme.Error
import com.netanel.xplore.ui.theme.OnPrimary
@Composable
fun PointsAnimationScreen(
    currentQuestionIndex: Int,
    quizResult: QuizResult,
    onAnimationEnd: () -> Unit
) {
    val result = quizResult.questionResults.getOrNull(currentQuestionIndex)
    val isCorrect = result?.isCorrect ?: false
    val points = result?.pointsAwarded ?: 0
    val correctAnswer =
        result?.question?.answers?.getOrNull(result.question.correctAnswerIndex ?: 0) ?: ""
    val explanation = result?.question?.explanation ?: ""
    val animationFile = if (isCorrect) "correct.json" else "wrong.json"
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(animationFile))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    val scale = remember { Animatable(0f) }

    LaunchedEffect(progress) {
        if (progress == 1f) {
            onAnimationEnd()
        }
    }

    LaunchedEffect(currentQuestionIndex, quizResult) {
        scale.snapTo(0f)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
    }

    AnimatedComposable(isVisible = true, content = {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(0.7f)
                )
            }

            Text(
                text = if (isCorrect) {
                    stringResource(R.string.points_added, points)
                } else {
                    stringResource(R.string.correct_answer_is, correctAnswer)
                },
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isCorrect) OnPrimary else Error,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.scale(scale.value)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (explanation.isNotBlank() && !isCorrect) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.explanation),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = explanation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    })
}
