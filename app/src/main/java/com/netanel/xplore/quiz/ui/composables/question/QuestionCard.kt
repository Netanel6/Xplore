package com.netanel.xplore.quiz.ui.composables.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.netanel.xplore.R
import com.netanel.xplore.quiz.ui.QuizUIState
import com.netanel.xplore.ui.theme.BackgroundLight
import com.netanel.xplore.ui.theme.OnPrimary

@Composable
fun QuestionCard(uiState: QuizUIState) {
    val question = uiState.currentQuestion

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundLight),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.SpaceEvenly) {
            Box(Modifier.fillMaxWidth()) {
                // ✨ Difficulty Indicator
                question?.difficulty?.let {
                    DifficultyBar(it)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = stringResource(
                        R.string.question_number,
                        uiState.currentQuestionNumber,
                        uiState.totalQuestions
                    ),
                    style = MaterialTheme.typography.headlineSmall,
                    color = OnPrimary
                )


            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = question?.text.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}