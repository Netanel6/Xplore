package com.netanel.xplore.quiz.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.quiz.ui.composables.LoadingScreen
import com.netanel.xplore.quiz.ui.composables.QuizEndScreen
import com.netanel.xplore.quiz.ui.composables.QuizQuestion
import com.netanel.xplore.ui.AnimatedComposable
import kotlinx.coroutines.delay

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
    var isUiVisible by remember { mutableStateOf(false) }

    // 🎬 Smooth Entry Animation
    LaunchedEffect(Unit) {
        delay(300)
        isUiVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        when (quizState) {
            is QuizState.Loading -> LoadingScreen()
            is QuizState.Error -> QuizErrorScreen(errorMessage = (quizState as QuizState.Error).message)
            is QuizState.Loaded -> {
                val quiz = (quizState as QuizState.Loaded).quiz
                val questions = quiz.questions

                // ✅ If all questions are answered, show the final score screen
                val allQuestionsAnswered = questions.all { it.isAnswered }

                if (allQuestionsAnswered) {
                    QuizEndScreen(
                        totalScore = quiz.totalScore,
                        onTryAgain = { viewModel.loadQuiz(quizId) },
                        onGoHome = { /* Navigate Home */ }
                    )
                    return
                }

                val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: return

                // 🌟 Animated Question Transition
                AnimatedComposable(
                    isVisible = isUiVisible,
                    enter = fadeIn(animationSpec = tween(700)),
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 📍 Display Current Question
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
                )
            }
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
