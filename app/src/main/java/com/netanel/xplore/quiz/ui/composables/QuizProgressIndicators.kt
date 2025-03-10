package com.netanel.xplore.quiz.ui.composables


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.concurrent.TimeUnit

@Composable
fun QuizProgressIndicators(
    currentTimeLeft: Long,
    totalTime: Long,
    answerLockTimeLeft: Long,
    initialLockTime: Long
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val safeTotalTime = if (totalTime > 0) totalTime else 1
        val safeLockTime = if (initialLockTime > 0) initialLockTime else 1

        val quizProgress by animateFloatAsState(
            targetValue = if (safeTotalTime > 0) currentTimeLeft.toFloat() / safeTotalTime else 0f,
            animationSpec = tween(durationMillis = 500),
            label = "QuizProgressAnimation"
        )

        val formattedTime = formatTime(currentTimeLeft)

        QuizProgressBar(quizProgress, formattedTime)

        AnswerLockTimer(answerLockTimeLeft, safeLockTime)
    }
}


@Composable
fun formatTime(milliseconds: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    val seconds =
        TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%02d:%02d", minutes, seconds)
}