package com.netanel.xplore.quiz.ui.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
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
        val safeTotalTime = if (totalTime > 0) totalTime else 1
        val safeLockTime = if (initialLockTime > 0) initialLockTime else 1

        val quizProgress by animateFloatAsState(
            targetValue = currentTimeLeft.toFloat() / safeTotalTime,
            animationSpec = tween(durationMillis = 500),
            label = "QuizProgressAnimation"
        )

        // **🔵 Quiz Timer Bar with Gradient & Glow**
        val gradientBrush = ShaderBrush(
            LinearGradientShader(
                colors = listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    MaterialTheme.colorScheme.primary
                ),
                from = androidx.compose.ui.geometry.Offset(0f, 0f),
                to = androidx.compose.ui.geometry.Offset(400f, 0f),
                tileMode = TileMode.Clamp
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        ) {
            LinearProgressIndicator(
                progress = { quizProgress },
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent, // Hide default color
                trackColor = Color.Transparent
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(quizProgress)
                    .fillMaxSize()
                    .background(gradientBrush)
            )
        }

        // **🔒 Pulsating Answer Lock Timer**
        val answerLockProgress by animateFloatAsState(
            targetValue = answerLockTimeLeft.toFloat() / safeLockTime,
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
