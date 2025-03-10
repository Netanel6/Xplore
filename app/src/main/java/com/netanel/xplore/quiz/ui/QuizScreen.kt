package com.netanel.xplore.quiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.quiz.ui.composables.PointsAnimationScreen
import com.netanel.xplore.quiz.ui.composables.QuizEndScreen
import com.netanel.xplore.quiz.ui.composables.QuizFinishedAnimation
import com.netanel.xplore.quiz.ui.composables.QuizProgressIndicators
import com.netanel.xplore.quiz.ui.composables.QuizQuestion
import com.netanel.xplore.quiz.utils.QuizTimerManager

@Composable
fun QuizScreen(
    quiz: Quiz,
    viewModel: QuizViewModel = hiltViewModel(),
    onGoHome: () -> Unit
) {
    // Load the quiz when the composable is first launched
    LaunchedEffect(quiz) {
        viewModel.loadQuiz(quiz)
    }

    // Collect the state from the ViewModel
    val quizState by viewModel.quizState.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()

    // UI state variables
    var showPointsAnimation by remember { mutableStateOf(false) }
    var quizCompleted by remember { mutableStateOf(false) }
    var showQuizFinishedAnimation by remember { mutableStateOf(false) }

    // Initialize the timer manager once
    val timerManager = remember { QuizTimerManager(quiz.quizTimer) }

    // Collect timer states
    val totalTimeLeft by timerManager.totalTimeLeft.collectAsState()
    val answerLockTimeLeft by timerManager.answerLockTimeLeft.collectAsState()

    // Get the current question
    val currentQuestion =
        (quizState as? QuizState.Loaded)?.quiz?.questions?.getOrNull(currentQuestionIndex)

    // Track viewed questions
    val viewedQuestions = remember { mutableSetOf<Int>() }
    var isFirstView by remember { mutableStateOf(false) }

    // Set the quiz finished listener once
    LaunchedEffect(Unit) {
        timerManager.setOnQuizFinishedListener {
            showQuizFinishedAnimation = true
        }
    }

    // Start the quiz timer when the quiz is loaded
    LaunchedEffect(quizState) {
        if (quizState is QuizState.Loaded) {
            timerManager.startQuizTimer()
        }
    }

    // Handle question viewing and answer lock timer
    LaunchedEffect(currentQuestionIndex) {
        isFirstView = viewedQuestions.add(currentQuestionIndex)
        if (isFirstView && currentQuestion?.isAnswered == false) {
            timerManager.startAnswerLockTimer(quiz.answerLockTimer) {
                // Actions to perform when the answer lock timer ends
            }
        } else {
            timerManager.stopAnswerLockTimer()
        }
    }

    // Check if the quiz is completed
    LaunchedEffect(totalTimeLeft, currentQuestionIndex) {
        val loadedQuiz = (quizState as? QuizState.Loaded)?.quiz ?: return@LaunchedEffect
        val allAnswered = loadedQuiz.questions.all { it.isAnswered }

        if (allAnswered || totalTimeLeft <= 0) {
            quizCompleted = true
            timerManager.stopQuizTimer()
            timerManager.stopAnswerLockTimer()
        }
    }

    // Prepare the UI state
    val uiState = QuizUIState(
        currentQuestion = currentQuestion,
        currentQuestionNumber = currentQuestionIndex + 1,
        totalQuestions = (quizState as? QuizState.Loaded)?.quiz?.questions?.size ?: 0,
        timeLeft = totalTimeLeft,
        quizCompleted = quizCompleted,
        showPointsAnimation = showPointsAnimation,
        showQuizFinishedAnimation = showQuizFinishedAnimation,
        isPreviousEnabled = currentQuestionIndex > 0 && !timerManager.isAnswerLocked,
        isNextEnabled = currentQuestion?.userSelectedAnswer != null && !timerManager.isAnswerLocked,
        isAnswered = currentQuestion?.isAnswered ?: false,
        questionLocked = timerManager.isAnswerLocked,
        answerLockTimeLeft = answerLockTimeLeft,
        isFirstView = isFirstView
    )

    // UI layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (!showQuizFinishedAnimation && !quizCompleted && !showPointsAnimation) {
            QuizProgressIndicators(
                currentTimeLeft = totalTimeLeft,
                totalTime = quiz.quizTimer,
                answerLockTimeLeft = if (uiState.isFirstView) answerLockTimeLeft else 0L,
                initialLockTime = quiz.answerLockTimer
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            showQuizFinishedAnimation -> {
                QuizFinishedAnimation(onAnimationEnd = {
                    quizCompleted = true
                    showQuizFinishedAnimation = false
                })
            }

            showPointsAnimation -> {
                PointsAnimationScreen(
                    explanation = currentQuestion?.explanation ?: "",
                    points = currentQuestion?.points ?: 0,
                    isCorrect = currentQuestion?.isCorrect ?: false,
                    correctAnswer = currentQuestion?.answers
                        ?.get(currentQuestion.correctAnswerIndex ?: 0).toString(),
                    onAnimationEnd = {
                        showPointsAnimation = false
                        if (currentQuestionIndex == uiState.totalQuestions - 1) {
                            quizCompleted = true
                        } else {
                            viewModel.nextQuestion()
                        }
                    }
                )
            }

            quizCompleted -> {
                QuizEndScreen(
                    totalScore = (quizState as QuizState.Loaded).quiz.totalScore,
                    onTryAgain = {
                        quizCompleted = false
                        showPointsAnimation = false
                        timerManager.resetTimers()
                        viewModel.resetQuiz()
                        viewedQuestions.clear()
                    },
                    onGoHome = {
                        timerManager.resetTimers()
                        onGoHome()
                    }
                )
            }

            else -> {
                Column {
                    QuizQuestion(
                        uiState = uiState,
                        onAnswerSelected = viewModel::selectAnswer,
                        onPreviousClicked = {
                            if (uiState.isPreviousEnabled) {
                                viewModel.previousQuestion()
                            }
                        },
                        onNextClicked = {
                            if (uiState.isNextEnabled && !currentQuestion!!.isAnswered) {
                                viewModel.lockAnswer()
                                showPointsAnimation = true
                            } else {
                                viewModel.nextQuestion()
                            }
                        }
                    )
                }
            }
        }
    }
}

data class QuizUIState(
    val currentQuestion: Question?,
    val currentQuestionNumber: Int,
    val totalQuestions: Int,
    val timeLeft: Long,
    val quizCompleted: Boolean,
    val showPointsAnimation: Boolean,
    val showQuizFinishedAnimation: Boolean,
    val isPreviousEnabled: Boolean,
    val isNextEnabled: Boolean,
    val isAnswered: Boolean,
    val questionLocked: Boolean,
    val answerLockTimeLeft: Long,
    val isFirstView: Boolean
)

@Composable
fun QuizErrorScreen(errorMessage: String) {
    Text(
        text = errorMessage,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(16.dp)
    )
}

sealed class QuizState {
    data object Loading : QuizState()
    data class Loaded(val quiz: Quiz) : QuizState()
    data class Error(val message: String) : QuizState()
}
