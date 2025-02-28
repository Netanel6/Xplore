package com.netanel.xplore.quiz.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.netanel.xplore.quiz.ui.QuizUIState

@Composable
fun QuizQuestion(
    uiState: QuizUIState,
    onAnswerSelected: (Int) -> Unit,
    onPreviousClicked: () -> Unit,
    onNextClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // 🔹 Question Card
        QuestionCard(uiState)

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Answer Options
        AnswerOptions(
            question = uiState.currentQuestion,
            isAnswered = uiState.isAnswered,
            onAnswerSelected = onAnswerSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Navigation Bar
        QuizNavigationBar(
            onPreviousClicked = onPreviousClicked,
            onNextClicked = onNextClicked,
            isPreviousEnabled = uiState.isPreviousEnabled,
            isNextEnabled = uiState.isNextEnabled
        )
    }
}