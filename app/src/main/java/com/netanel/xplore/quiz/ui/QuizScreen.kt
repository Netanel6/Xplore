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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

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
    var questionLocked by remember { mutableStateOf(true) }
    var quizTimerPaused by remember { mutableStateOf(false) }

    // ✅ Extract Quiz Timer when quiz is loaded
    val totalQuizTime = (quizState as? QuizState.Loaded)?.quiz?.timer ?: 60
    var totalTimeLeft by remember { mutableIntStateOf(totalQuizTime) }
    var answerLockTimeLeft by remember { mutableIntStateOf(10) }
    var job: Job? by remember { mutableStateOf(null) } // Track timer coroutine

    val currentQuestion = (quizState as? QuizState.Loaded)?.quiz?.questions?.getOrNull(currentQuestionIndex)

    // ✅ Detect Last Question
    val isLastQuestion by remember(currentQuestionIndex, quizState) {
        derivedStateOf {
            val questions = (quizState as? QuizState.Loaded)?.quiz?.questions ?: emptyList()
            currentQuestionIndex == questions.lastIndex
        }
    }

    // ✅ Track Answer Lock Completion for Each Question
    val answerLockCompleted = remember { mutableStateMapOf<Int, Boolean>() }

    // **🔥 Global Quiz Timer**
    LaunchedEffect(Unit) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Default).launch {
            while (totalTimeLeft > 0 && isActive) {
                if (!quizTimerPaused) {
                    delay(1000L)
                    totalTimeLeft--
                }
            }
            showQuizFinishedAnimation = true
        }
    }

    // **🔐 Answer Lock Timer (Fix: Prevent Restart on Answered Questions)**
    LaunchedEffect(currentQuestionIndex) {
        if (currentQuestion?.isAnswered == true || answerLockCompleted[currentQuestionIndex] == true) {
            // ✅ If the question was already answered or lock already completed, don't lock again
            questionLocked = false
            answerLockTimeLeft = 0
        } else {
            // ✅ If it's a new question, start the answer lock timer
            questionLocked = true
            answerLockTimeLeft = 10
            quizTimerPaused = true
            while (answerLockTimeLeft > 0 && isActive) {
                delay(1000L)
                answerLockTimeLeft--
            }
            questionLocked = false
            quizTimerPaused = false
            answerLockCompleted[currentQuestionIndex] = true
        }
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
                isPreviousEnabled = currentQuestionIndex > 0 && !questionLocked,
                isNextEnabled = !questionLocked || currentQuestion?.isAnswered == true,
                isAnswered = currentQuestion?.isAnswered ?: false,
                questionLocked = questionLocked,
                answerLockTimeLeft = answerLockTimeLeft
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // ✅ Show Progress Bar
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
                    // ✅ Quiz Finished Animation
                    showQuizFinishedAnimation -> {
                        QuizFinishedAnimation(
                            onAnimationEnd = {
                                quizCompleted = true
                                showQuizFinishedAnimation = false
                            }
                        )
                    }

                    // ✅ Points Animation
                    showPointsAnimation -> {
                        PointsAnimationScreen(
                            points = currentQuestion?.pointsGained ?: 0,
                            isCorrect = currentQuestion?.isCorrect ?: false,
                            correctAnswer = currentQuestion?.answers?.get(currentQuestion.correctAnswerIndex ?: 0).toString(),
                            onAnimationEnd = {
                                showPointsAnimation = false
                                if (isLastQuestion) {
                                    quizCompleted = true
                                } else {
                                    viewModel.nextQuestion()
                                }
                            }
                        )
                    }

                    // ✅ Quiz End Screen
                    quizCompleted -> {
                        QuizEndScreen(
                            totalScore = quiz.totalScore,
                            onTryAgain = {
                                quizCompleted = false
                                showPointsAnimation = false
                                totalTimeLeft = totalQuizTime
                                answerLockTimeLeft = 10
                                questionLocked = true
                                answerLockCompleted.clear()
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
                                if (!questionLocked && currentQuestion?.userSelectedAnswer != null && !currentQuestion.isAnswered) {
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
