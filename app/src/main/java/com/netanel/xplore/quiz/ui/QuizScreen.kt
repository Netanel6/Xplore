package com.netanel.xplore.quiz.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.quiz.ui.composables.LoadingScreen
import com.netanel.xplore.quiz.ui.composables.PointsAnimationScreen
import com.netanel.xplore.quiz.ui.composables.QuizEndScreen
import com.netanel.xplore.quiz.ui.composables.QuizQuestion

@Composable
fun QuizScreen(
    quizId: String,
    viewModel: QuizViewModel = hiltViewModel(),
    onGoHome: () -> Unit
) {
    LaunchedEffect(quizId) {
        viewModel.loadQuiz(quizId)
    }

    val quizState by viewModel.quizState.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    var showPointsAnimation by remember { mutableStateOf(false) }
    var quizCompleted by remember { mutableStateOf(false) }

    when (quizState) {
        is QuizState.Loading -> LoadingScreen()
        is QuizState.Error -> QuizErrorScreen(errorMessage = (quizState as QuizState.Error).message)
        is QuizState.Loaded -> {
            val quiz = (quizState as QuizState.Loaded).quiz
            val questions = quiz.questions
            val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: return

            // ✅ Check if we need to show the final screen
            if (quizCompleted) {
                QuizEndScreen(
                    totalScore = quiz.totalScore,
                    onTryAgain = {
                        quizCompleted = false
                        viewModel.resetQuiz()
                    },
                    onGoHome = { onGoHome() }
                )
                return
            }

            // ✅ Show points animation before moving forward
            if (showPointsAnimation) {
                PointsAnimationScreen(
                    points = currentQuestion.pointsGained,
                    isCorrect = currentQuestion.isCorrect,
                    correctAnswer = currentQuestion.answers?.get(
                        currentQuestion.correctAnswerIndex ?: return
                    ) ?: "",
                    onAnimationEnd = {
                        showPointsAnimation = false

                        if (questions.all { it.isAnswered }) {
                            quizCompleted = true
                        } else {
                            viewModel.nextQuestion()
                        }
                    }
                )
                return
            }

            QuizQuestion(
                question = currentQuestion,
                currentQuestionNumber = currentQuestionIndex + 1,
                totalQuestions = questions.size,
                onAnswerSelected = { answerIndex ->
                    viewModel.selectAnswer(answerIndex)
                },
                onNextClicked = {
                    viewModel.lockAnswer()
                    showPointsAnimation = true
                },
                onPreviousClicked = {
                    viewModel.previousQuestion()
                }
            )
        }
    }
}

@Composable
fun QuizErrorScreen(errorMessage: String) {
    Text(
        text = errorMessage,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(16.dp)
    )
}

sealed class QuizState {
    data object Loading : QuizState()
    data class Loaded(val quiz: Quiz) : QuizState()
    data class Error(val message: String) : QuizState()
}