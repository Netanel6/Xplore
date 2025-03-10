package com.netanel.xplore.quiz.ui.composables

import AnswerButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.netanel.xplore.quiz.model.Question

@Composable
fun AnswerOptions(
    question: Question?,
    isLocked: Boolean,
    onAnswerSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        question?.answers?.forEachIndexed { index, answer ->
            val isSelected = question.userSelectedAnswer == index
            AnswerButton(
                answer = answer,
                isSelected = isSelected,
                isLocked = isLocked,
                onClick = { if (!isLocked) onAnswerSelected(index) }
            )
        }
    }
}
