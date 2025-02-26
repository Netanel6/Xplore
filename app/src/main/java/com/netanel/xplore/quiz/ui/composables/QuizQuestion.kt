package com.netanel.xplore.quiz.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.netanel.xplore.R
import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.ui.theme.AnswerBorder
import com.netanel.xplore.ui.theme.AnswerSelected
import com.netanel.xplore.ui.theme.AnswerUnselected
import com.netanel.xplore.ui.theme.CardBackground

@Composable
fun QuizQuestion(
    question: Question,
    currentQuestionNumber: Int,
    totalQuestions: Int,
    onAnswerSelected: (Int) -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit
) {
    var selectedAnswer by remember { mutableStateOf(question.userSelectedAnswer) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 🔹 Navigation Bar (Back, Timer, Submit)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onPreviousClicked() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.previous_question),
                    tint = Color.White
                )
            }

            Text(
                text = stringResource(R.string.quiz_timer),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = { onNextClicked() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(stringResource(R.string.submit), color = MaterialTheme.colorScheme.primary)
            }
        }

        // 🔹 Progress Indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(totalQuestions) { index ->
                val isAnswered = index < currentQuestionNumber
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = if (isAnswered) Color.White else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                )
            }
        }

        // 🔹 Question Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
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
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start
                )
            }
        }

        // 🔹 Answer Options
        Column(modifier = Modifier.fillMaxWidth()) {
            question.answers?.forEachIndexed { index, answer ->
                val isSelected = selectedAnswer == index
                val isLocked = question.isAnswered

                Button(
                    onClick = {
                        if (!isLocked) {
                            selectedAnswer = index
                            onAnswerSelected(index)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) AnswerSelected else AnswerUnselected,
                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
                    ),
                    border = if (!isSelected) BorderStroke(1.dp, AnswerBorder) else null
                ) {
                    Text(
                        text = answer,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }
    }
}
