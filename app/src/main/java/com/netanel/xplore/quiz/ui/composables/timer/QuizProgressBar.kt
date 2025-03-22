package com.netanel.xplore.quiz.ui.composables.timer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.netanel.xplore.ui.theme.BackgroundLight
import com.netanel.xplore.ui.theme.GradientEnd
import com.netanel.xplore.ui.theme.GradientMid
import com.netanel.xplore.ui.theme.GradientStart

@Composable
fun QuizProgressBar(
    quizProgress: Float,  // Progress value (0.0 - 1.0)
    formattedTime: String  // Displayed time text
) {
    val animatedProgress by animateFloatAsState(
        targetValue = quizProgress,
        animationSpec = tween(durationMillis = 800),
        label = "AnimatedQuizProgress"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // 🟣 Stylish Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BackgroundLight.copy(alpha = 0.3f)),
            contentAlignment = Alignment.CenterStart
        ) {
            val gradient = Brush.horizontalGradient(
                colors = listOf(
                    GradientStart,
                    GradientMid,
                    GradientEnd
                )
            )

            // 🔵 Animated Gradient Progress Fill
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(gradient)
            )
        }

        // ⏳ Time & Progress Text
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = "Quiz Timer",
                tint = GradientMid
            )
            Text(
                text = "זמן חידון: $formattedTime",
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}
