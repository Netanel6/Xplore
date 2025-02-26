package com.netanel.xplore.quiz.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.netanel.xplore.R
import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.ui.theme.AnswerLocked
import com.netanel.xplore.ui.theme.AnswerSelected
import com.netanel.xplore.ui.theme.AnswerUnselected

@Composable
fun QuizQuestion(
    question: Question,
    currentQuestionNumber: Int,
    totalQuestions: Int,
    onAnswerSelected: (Int) -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit
) {
    val selectedAnswer = question.userSelectedAnswer
    val isAnswered = question.isAnswered

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 🔹 Navigation Bar
        QuizNavigationBar(
            onPreviousClicked = onPreviousClicked,
            onNextClicked = { if (selectedAnswer != null) onNextClicked() },
            isNextEnabled = selectedAnswer != null || isAnswered
        )

        // 🔹 Progress Indicators
        QuizProgressIndicators(currentQuestionNumber, totalQuestions)

        // 🔹 Question Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.question_number, currentQuestionNumber, totalQuestions),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = question.text.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // 🔹 Answer Options
        Column(modifier = Modifier.fillMaxWidth()) {
            question.answers?.forEachIndexed { index, answer ->
                val isSelected = selectedAnswer == index
                AnswerButton(
                    answer = answer,
                    isSelected = isSelected,
                    isLocked = isAnswered,
                    onClick = { if (!isAnswered) onAnswerSelected(index) }
                )
            }
        }
    }
}

@Composable
fun AnswerButton(
    answer: String,
    isSelected: Boolean,
    isLocked: Boolean,
    onClick: () -> Unit
) {
    val buttonColor = when {
        isLocked && isSelected -> AnswerLocked
        isSelected -> AnswerSelected
        else -> AnswerUnselected
    }

    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
            contentColor = if (!isSelected) Color.White else MaterialTheme.colorScheme.primary
        ),
        border = if (!isSelected) BorderStroke(1.dp, AnswerUnselected) else BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        enabled = !isLocked
    ) {
        Text(
            text = answer,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}
