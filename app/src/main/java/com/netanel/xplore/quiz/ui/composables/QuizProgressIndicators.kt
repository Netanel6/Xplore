package com.netanel.xplore.quiz.ui.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.netanel.xplore.R
import com.netanel.xplore.ui.AnimatedComposable

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
        // **⏳ Total Quiz Timer Progress**
        val quizProgress by animateFloatAsState(
            targetValue = currentTimeLeft.toFloat() / totalTime,
            animationSpec = tween(durationMillis = 500),
            label = "QuizProgressAnimation"
        )

        Text(
            text = stringResource(R.string.quiz_timer_text, currentTimeLeft),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .padding(2.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            LinearProgressIndicator(
                progress = { quizProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
                color = MaterialTheme.colorScheme.primary
            )
        }

        // **🔐 Answer Lock Timer (Using AnimatedComposable)**
        AnimatedComposable(isVisible = answerLockTimeLeft > 0, content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.answer_unlock_text, answerLockTimeLeft),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.secondary
                )

                val answerLockProgress by animateFloatAsState(
                    targetValue = answerLockTimeLeft.toFloat() / initialLockTime,
                    animationSpec = tween(durationMillis = 500),
                    label = "AnswerLockProgressAnimation"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(Color.LightGray, RoundedCornerShape(6.dp))
                        .padding(2.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    LinearProgressIndicator(
                        progress = { answerLockProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(Color.LightGray, RoundedCornerShape(6.dp)),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        })
    }
}
