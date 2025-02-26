package com.netanel.xplore.quiz.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.netanel.xplore.quiz.model.Question

@Composable
fun QuizQuestion(
    question: Question,
    currentQuestionNumber: Int,
    totalQuestions: Int,
    onAnswerSelected: (Int) -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit
) {
    // 🔹 State for the selected answer, reset when question changes
    var selectedAnswer by remember(question.id) { mutableStateOf(question.userSelectedAnswer) }

    // 🔹 State to control "Next" button, resets when question changes
    var isNextEnabled by remember(question.id) { mutableStateOf(selectedAnswer != null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 🔹 Navigation Bar (Previous Button, Question Number, Next Button)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onPreviousClicked() },
                enabled = currentQuestionNumber > 1,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("קודם")
            }

            Text(
                text = "שאלה $currentQuestionNumber מתוך $totalQuestions",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = {
                    onNextClicked()
                    selectedAnswer = null
                    isNextEnabled = false
                },
                enabled = isNextEnabled,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("לשאלה הבאה")
            }
        }

        // 🔹 Question Text
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                text = question.text.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        // 🔹 Answer Options
        Column(modifier = Modifier.fillMaxWidth()) {
            question.answers?.forEachIndexed { index, answer ->
                val isSelected = selectedAnswer == index
                val isLocked = question.isAnswered

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isLocked) {
                            selectedAnswer = index
                            isNextEnabled = true
                            onAnswerSelected(index)
                        },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            isLocked && isSelected -> MaterialTheme.colorScheme.secondary
                            isSelected -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.surface
                        }
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = null // Controlled by card click
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = answer,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }
        }
    }
}
