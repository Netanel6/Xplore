package com.netanel.xplore.quiz.ui.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.netanel.xplore.ui.theme.OnPrimary
import com.netanel.xplore.ui.theme.OnSecondary

@Composable
fun QuizProgressIndicators(
    currentTimeLeft: Int,
    totalTime: Int,
    answerLockTimeLeft: Int,
    initialLockTime: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val safeTotalTime = if (totalTime > 0) totalTime else -1
        val safeLockTime = if (initialLockTime > 0) initialLockTime else 10

        val quizProgress by animateFloatAsState(
            targetValue = if (safeTotalTime > 0) currentTimeLeft.toFloat() / safeTotalTime else 0f,
            animationSpec = tween(durationMillis = 500),
            label = "QuizProgressAnimation"
        )

        if (safeTotalTime > 0) {
            QuizTimerProgressBar(progress = quizProgress)
        }

        // 🔒 **Pulsating Answer Lock Timer**
        val answerLockProgress by animateFloatAsState(
            targetValue = if (safeLockTime > 0) answerLockTimeLeft.toFloat() / safeLockTime else 0f,
            animationSpec = tween(durationMillis = 500),
            label = "AnswerLockProgressAnimation"
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(if (answerLockTimeLeft > 0) 1f else 0f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(32.dp)) {
                    drawCircle(
                        color = OnSecondary.copy(alpha = 0.2f),
                        radius = size.minDimension / 2
                    )
                    drawArc(
                        color = OnPrimary,
                        startAngle = -90f,
                        sweepAngle = 360f * answerLockProgress,
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
                Text(
                    text = "$answerLockTimeLeft",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun GradientProgressBar(progress: Float) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            MaterialTheme.colorScheme.primary
        ),
        startX = 0f,
        endX = 400f
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(14.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .background(gradientBrush)
        )
    }
}
