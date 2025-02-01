package com.netanel.xplore.quiz.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.quiz.ui.composables.LoadingScreen
import com.netanel.xplore.quiz.ui.composables.PointsAnimationScreen
import com.netanel.xplore.quiz.ui.composables.QuizEndScreen
import com.netanel.xplore.quiz.ui.composables.QuizQuestion
@Composable
fun QuizScreen(
    userId: String,
    quizId: String,
    viewModel: QuizViewModel = hiltViewModel()
) {
    LaunchedEffect(quizId) {
        viewModel.loadQuiz(quizId)
    }

    val quiz by viewModel.quiz.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val totalScore by viewModel.totalScore.collectAsState()

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var isAnswerLocked by remember { mutableStateOf(false) }
    var showAnimation by remember { mutableStateOf(false) }
    var pointsGained by remember { mutableStateOf(0) }
    var isCorrect by remember { mutableStateOf(false) }
    val answerStates = remember { mutableMapOf<Int, Pair<Int?, Boolean>>() }
    val scoredQuestions = remember { mutableSetOf<Int>() }

    if (quiz == null && isLoading) {
        LoadingScreen()
        return
    }

    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    if (currentQuestion == null) {
        QuizEndScreen(totalScore = totalScore)
        return
    }

    // Restore state only after answering or navigating
    LaunchedEffect(currentQuestionIndex) {
        val previousState = answerStates[currentQuestionIndex]
        selectedAnswer = previousState?.first
        isAnswerLocked = previousState?.second ?: false
    }

    if (showAnimation) {
        val correctAnswerText = currentQuestion.answers?.get(currentQuestion.correctAnswerIndex ?: -1).orEmpty()

        PointsAnimationScreen(
            points = pointsGained,
            isCorrect = isCorrect,
            correctAnswer = correctAnswerText,
            onAnimationEnd = {
                showAnimation = false
                currentQuestionIndex++
                selectedAnswer = null
                isAnswerLocked = false
            }
        )
    } else {
        QuizQuestion(
            question = currentQuestion,
            currentQuestionNumber = currentQuestionIndex + 1,
            totalQuestions = questions.size,
            userSelectedAnswer = selectedAnswer,
            isAnswerLocked = isAnswerLocked,
            onAnswerSelected = { selectedIndex ->
                if (!isAnswerLocked) {
                    selectedAnswer = selectedIndex
                }
            },
            onNextClicked = {
                if (selectedAnswer != null) {
                    answerStates[currentQuestionIndex] = selectedAnswer to true
                    isAnswerLocked = true

                    if (currentQuestionIndex !in scoredQuestions) {
                        isCorrect = selectedAnswer == currentQuestion.correctAnswerIndex
                        pointsGained = if (isCorrect) currentQuestion.points else 0
                        if (isCorrect) {
                            viewModel.addScore(pointsGained)
                        }
                        scoredQuestions.add(currentQuestionIndex)
                        showAnimation = true
                    } else {
                        currentQuestionIndex++
                        selectedAnswer = null
                        isAnswerLocked = false
                    }
                }
            },
            onPreviousClicked = {
                if (currentQuestionIndex > 0) {
                    answerStates[currentQuestionIndex] = selectedAnswer to isAnswerLocked
                    currentQuestionIndex--
                }
            }
        )
    }
}
