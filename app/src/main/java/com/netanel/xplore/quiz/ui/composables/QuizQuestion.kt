package com.netanel.xplore.quiz.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
                    selectedAnswer = null  // 🔹 Reset answer when moving to the next question
                    isNextEnabled = false  // 🔹 Disable "Next" until a new answer is selected
                },
                enabled = isNextEnabled, // ✅ Button enabled only if an answer is selected
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
                        .clickable(enabled = !isLocked) { // ✅ Click only if question is not locked
                            selectedAnswer = index
                            isNextEnabled = true  // 🔹 Enable "Next" once an answer is selected
                            onAnswerSelected(index)
                        },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            isLocked && isSelected -> MaterialTheme.colorScheme.secondary // ✅ Locked Answer Color
                            isSelected -> MaterialTheme.colorScheme.tertiary // ✅ Selected Answer Color
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
