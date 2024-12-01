package com.netanel.xplore.quiz.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.quiz.ui.composables.QuizEndScreen
import com.netanel.xplore.quiz.ui.composables.QuizQuestion
@Composable
fun QuizScreen(viewModel: QuizViewModel = hiltViewModel()) {
    val questions by viewModel.questions.collectAsState()
    val totalScore by viewModel.totalScore.collectAsState()
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var showAnimation by remember { mutableStateOf(false) }
    var animationType by remember { mutableStateOf(AnimationType.Idle) }
    var pointsGained by remember { mutableStateOf(0) }
    var isCorrect by remember { mutableStateOf(false) }

    val lockedQuestions = remember { mutableStateListOf<Int>() }
    val animatedQuestions = remember { mutableStateListOf<Int>() }
    val selectedAnswers = remember(questions) { MutableList(questions.size) { mutableStateOf<Int?>(null) } }
    val scoredQuestions = remember { mutableStateListOf<Int>() }

    if (questions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Text(
                text = "שאלות נטענות אנא חכה",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    } else {
        AnimatedContent(targetState = showAnimation, label = "Animated content") { isAnimation ->
            if (isAnimation) {
                PointsAnimationScreen(
                    points = pointsGained,
                    isCorrect = isCorrect,
                    onAnimationEnd = {
                        showAnimation = false
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                        } else {
                            currentQuestionIndex = questions.size
                        }
                    }
                )
            } else {
                if (currentQuestionIndex < questions.size) {
                    val isAnswerLocked = lockedQuestions.contains(currentQuestionIndex)
                    QuizQuestion(
                        question = questions[currentQuestionIndex],
                        currentQuestionNumber = currentQuestionIndex + 1,
                        totalQuestions = questions.size,
                        userSelectedAnswer = selectedAnswers[currentQuestionIndex].value,
                        isAnswerLocked = isAnswerLocked,
                        onAnswerSelected = { selectedIndex ->
                            selectedAnswers[currentQuestionIndex].value = selectedIndex
                        },
                        onNextClicked = {
                            val selectedIndex = selectedAnswers[currentQuestionIndex].value
                            if (selectedIndex != null) {
                                isCorrect =
                                    selectedIndex == questions[currentQuestionIndex].correctAnswerIndex
                                animationType = if (isCorrect) AnimationType.Correct else AnimationType.Wrong

                                if (isCorrect && currentQuestionIndex !in scoredQuestions) {
                                    pointsGained = questions[currentQuestionIndex].points
                                    viewModel.addScore(pointsGained)
                                    scoredQuestions.add(currentQuestionIndex)
                                }

                                if (currentQuestionIndex !in animatedQuestions) {
                                    showAnimation = true
                                    animatedQuestions.add(currentQuestionIndex)
                                    lockedQuestions.add(currentQuestionIndex)
                                } else {
                                    if (currentQuestionIndex < questions.size - 1) currentQuestionIndex++
                                }
                            }
                        },
                        onPreviousClicked = {
                            if (currentQuestionIndex > 0) currentQuestionIndex--
                        }
                    )
                } else {
                    QuizEndScreen(totalScore = totalScore)
                }
            }
        }
    }
}

@Composable
fun PointsAnimationScreen(points: Int, isCorrect: Boolean, onAnimationEnd: () -> Unit) {
    val animationFile = if (isCorrect) "correct.json" else "wrong.json"
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(animationFile))
    val progress by animateLottieCompositionAsState(composition, iterations = 1)

    LaunchedEffect(progress) {
        if (progress == 1f) onAnimationEnd()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LottieAnimation(composition, progress, modifier = Modifier.size(300.dp))
            if (isCorrect) {
                Text(
                    text = "+$points נקודות",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            // TODO: Decided whether to show the text or not according to the animation (wrong animation)
            /* else {
                Text(
                    text = "תשובה לא נכונה!",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.error
                )
            }*/
        }
    }
}





enum class AnimationType {
    Correct,
    Wrong,
    Idle
}
