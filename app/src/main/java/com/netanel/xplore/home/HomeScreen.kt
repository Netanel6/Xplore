package com.netanel.xplore.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.quiz.ui.composables.LoadingScreen
import com.netanel.xplore.R

@Composable
fun HomeScreen(
    onQuizSelected: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val quizList by viewModel.quizList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var isPopupOpen by remember { mutableStateOf(false) }

    if (isLoading) {
        LoadingScreen()
        return
    }

    if (errorMessage != null) {
        /*Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(id = R.string.generic_error, errorMessage))
        }*/
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { isPopupOpen = true },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(stringResource(id = R.string.select_quiz))
        }

        if (isPopupOpen) {
            QuizListPopup(
                quizzes = quizList.orEmpty(),
                onQuizSelected = { quizId ->
                    isPopupOpen = false
                    onQuizSelected(quizId)
                },
                onClose = { isPopupOpen = false }
            )
        }
    }
}

@Composable
fun QuizListPopup(
    quizzes: List<Quiz>,
    onQuizSelected: (String) -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onClose() },
        title = { Text(stringResource(id = R.string.select_quiz_title)) },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
            ) {
                items(quizzes) { quiz ->
                    QuizListItem(
                        quiz = quiz,
                        onClick = { onQuizSelected(quiz.creatorId.toString()) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onClose() }) {
                Text(stringResource(id = R.string.retry))
            }
        }
    )
}

@Composable
fun QuizListItem(quiz: Quiz, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = quiz.title.toString(), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            quiz.description?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
        }
    }
}
