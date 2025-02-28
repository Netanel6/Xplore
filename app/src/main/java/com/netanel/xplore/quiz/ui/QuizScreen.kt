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
import com.netanel.xplore.quiz.ui.composables.LoadingScreen
import com.netanel.xplore.quiz.ui.composables.PointsAnimationScreen
import com.netanel.xplore.quiz.ui.composables.QuizEndScreen
import com.netanel.xplore.quiz.ui.composables.QuizFinishedAnimation
import com.netanel.xplore.quiz.ui.composables.QuizProgressIndicators
import com.netanel.xplore.quiz.ui.composables.QuizQuestion
import com.netanel.xplore.quiz.utils.QuizTimerManager

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
    var showQuizFinishedAnimation by remember { mutableStateOf(false) }

    val totalQuizTime = (quizState as? QuizState.Loaded)?.quiz?.timer ?: 60
    val timerManager = remember { QuizTimerManager(totalQuizTime) }

    val currentQuestion = (quizState as? QuizState.Loaded)?.quiz?.questions?.getOrNull(currentQuestionIndex)

    val totalTimeLeft by timerManager.totalTimeLeft.collectAsState()
    val answerLockTimeLeft by timerManager.answerLockTimeLeft.collectAsState()

    LaunchedEffect(Unit) {
        timerManager.startQuizTimer { showQuizFinishedAnimation = true }
    }

    LaunchedEffect(currentQuestionIndex) {
        timerManager.startAnswerLockTimer(currentQuestionIndex) {}
    }

    when (quizState) {
        is QuizState.Loading -> LoadingScreen()
        is QuizState.Error -> QuizErrorScreen(errorMessage = (quizState as QuizState.Error).message)
        is QuizState.Loaded -> {
            val quiz = (quizState as QuizState.Loaded).quiz
            val questions = quiz.questions

            val uiState = QuizUIState(
                currentQuestion = currentQuestion,
                currentQuestionNumber = currentQuestionIndex + 1,
                totalQuestions = questions.size,
                timeLeft = totalTimeLeft,
                quizCompleted = quizCompleted,
                showPointsAnimation = showPointsAnimation,
                showQuizFinishedAnimation = showQuizFinishedAnimation,
                isPreviousEnabled = currentQuestionIndex > 0 && !timerManager.isAnswerLocked,
                isNextEnabled = !timerManager.isAnswerLocked || currentQuestion?.isAnswered == true,
                isAnswered = currentQuestion?.isAnswered ?: false,
                questionLocked = timerManager.isAnswerLocked,
                answerLockTimeLeft = answerLockTimeLeft
            )


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (!showQuizFinishedAnimation && !quizCompleted && !showPointsAnimation) {
                    QuizProgressIndicators(
                        currentTimeLeft = totalTimeLeft,
                        totalTime = totalQuizTime,
                        answerLockTimeLeft = answerLockTimeLeft,
                        initialLockTime = 10
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    showQuizFinishedAnimation -> {
                        QuizFinishedAnimation(
                            onAnimationEnd = {
                                quizCompleted = true
                                showQuizFinishedAnimation = false
                            }
                        )
                    }

                    showPointsAnimation -> {
                        PointsAnimationScreen(
                            points = currentQuestion?.pointsGained ?: 0,
                            isCorrect = currentQuestion?.isCorrect ?: false,
                            correctAnswer = currentQuestion?.answers?.get(currentQuestion.correctAnswerIndex ?: 0).toString(),
                            onAnimationEnd = {
                                showPointsAnimation = false
                                if (currentQuestionIndex == questions.lastIndex) {
                                    quizCompleted = true
                                } else {
                                    viewModel.nextQuestion()
                                }
                            }
                        )
                    }

                    quizCompleted -> {
                        QuizEndScreen(
                            totalScore = quiz.totalScore,
                            onTryAgain = {
                                quizCompleted = false
                                showPointsAnimation = false
                                timerManager.resetQuizTimer()
                                timerManager.resetAnswerLock()
                                viewModel.resetQuiz()
                            },
                            onGoHome = { onGoHome() }
                        )
                    }

                    else -> {
                        QuizQuestion(
                            uiState = uiState,
                            onAnswerSelected = { answerIndex -> viewModel.selectAnswer(answerIndex) },
                            onPreviousClicked = {
                                if (currentQuestionIndex > 0) {
                                    viewModel.previousQuestion()
                                    showPointsAnimation = false
                                }
                            },
                            onNextClicked = {
                                if (!timerManager.isAnswerLocked && currentQuestion?.userSelectedAnswer != null && !currentQuestion.isAnswered) {
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
}

data class QuizUIState(
    val currentQuestion: Question?,
    val currentQuestionNumber: Int,
    val totalQuestions: Int,
    val timeLeft: Int,
    val quizCompleted: Boolean,
    val showPointsAnimation: Boolean,
    val showQuizFinishedAnimation: Boolean,
    val isPreviousEnabled: Boolean,
    val isNextEnabled: Boolean,
    val isAnswered: Boolean,
    val questionLocked: Boolean,
    val answerLockTimeLeft: Int
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
