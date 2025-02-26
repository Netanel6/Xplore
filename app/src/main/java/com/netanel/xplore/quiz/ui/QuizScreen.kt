package com.netanel.xplore.quiz.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.quiz.ui.composables.LoadingScreen
import com.netanel.xplore.quiz.ui.composables.QuizEndScreen
import com.netanel.xplore.quiz.ui.composables.QuizQuestion

@Composable
fun QuizScreen(
    quizId: String,
    viewModel: QuizViewModel = hiltViewModel()
) {
    LaunchedEffect(quizId) {
        viewModel.loadQuiz(quizId)
    }

    val quizState by viewModel.quizState.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()

    when (quizState) {
        is QuizState.Loading -> LoadingScreen()
        is QuizState.Error -> QuizErrorScreen(errorMessage = (quizState as QuizState.Error).message)
        is QuizState.Loaded -> {
            val quiz = (quizState as QuizState.Loaded).quiz
            val questions = quiz.questions

            // ✅ If all questions are answered, show the final score screen
            val allQuestionsAnswered = questions.all { it.isAnswered }

            if (allQuestionsAnswered) {
                QuizEndScreen(totalScore = quiz.totalScore)
                return
            }

            val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: return

            QuizQuestion(
                question = currentQuestion,
                currentQuestionNumber = currentQuestionIndex + 1,
                totalQuestions = questions.size,
                onAnswerSelected = { answerIndex ->
                    viewModel.selectAnswer(answerIndex)
                },
                onNextClicked = {
                    viewModel.lockAnswer()
                    viewModel.nextQuestion()
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
    Text(text = errorMessage)
}

sealed class QuizState {
    data object Loading : QuizState()
    data class Loaded(val quiz: Quiz) : QuizState()
    data class Error(val message: String) : QuizState()
}