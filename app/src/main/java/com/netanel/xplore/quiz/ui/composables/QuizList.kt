package com.netanel.xplore.quiz.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.netanel.xplore.quiz.model.Quiz

@Composable
fun QuizList(
    quizzes: List<Quiz>,
    onQuizSelected: (String) -> Unit
) {
    LazyColumn {
        items(quizzes) { quiz ->
            QuizItem(quiz = quiz, onQuizSelected = onQuizSelected)
        }
    }
}

@Composable
fun QuizItem(
    quiz: Quiz,
    onQuizSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onQuizSelected(quiz.title.toString()) },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = quiz.title.toString(),
            )
            Text(
                text = "${quiz.questions.size} questions",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
