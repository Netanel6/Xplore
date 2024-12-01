package com.netanel.xplore.quiz.ui

import androidx.compose.animation.AnimatedContent
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.netanel.xplore.quiz.model.Question

@Composable
fun QuizScreen(viewModel: QuizViewModel = hiltViewModel()) {
    val questions by viewModel.questions.collectAsState()
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var showAnimation by remember { mutableStateOf(false) }
    var animationType by remember { mutableStateOf(AnimationType.Idle) }

    val lockedQuestions = remember { mutableStateListOf<Int>() }
    val animatedQuestions = remember { mutableStateListOf<Int>() }
    val selectedAnswers = remember(questions) { MutableList(questions.size) { mutableStateOf<Int?>(null) } }

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
        AnimatedContent(targetState = showAnimation) { isAnimation ->
            if (isAnimation) {
                LottieAnimationScreen(
                    animationType = animationType,
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
                            if (selectedAnswers[currentQuestionIndex].value != null) {
                                val isCorrect =
                                    selectedAnswers[currentQuestionIndex].value == questions[currentQuestionIndex].correctAnswerIndex
                                animationType = if (isCorrect) AnimationType.Correct else AnimationType.Wrong

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
                    QuizEndScreen()
                }
            }
        }
    }
}

@Composable
fun QuizQuestion(
    question: Question,
    currentQuestionNumber: Int,
    totalQuestions: Int,
    userSelectedAnswer: Int?,
    isAnswerLocked: Boolean,
    onAnswerSelected: (Int) -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit
) {
    val shuffledAnswers = remember(currentQuestionNumber) {
        val answersWithIndex = question.answers.mapIndexed { index, answer -> index to answer }
        answersWithIndex.shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentQuestionNumber > 1) {
                Text(
                    text = "לשאלה הקודמת",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onPreviousClicked() }
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }
            Text(
                text = "$currentQuestionNumber/$totalQuestions",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Text(
                text = question.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            shuffledAnswers.forEachIndexed { _, (originalIndex, answer) ->
                val isSelected = userSelectedAnswer == originalIndex
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isAnswerLocked) { onAnswerSelected(originalIndex) },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.tertiary else Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = null // RadioButton is for display only
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = answer,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onNextClicked() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "לשאלה הבאה",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
            )
        }
    }
}

@Composable
fun QuizEndScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Quiz Completed",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = "סיימת את השאלון! גש לקבל סוכריה",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun LottieAnimationScreen(animationType: AnimationType, onAnimationEnd: () -> Unit) {
    val animationFile = if (animationType == AnimationType.Correct) "correct.json" else "wrong.json"
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
        LottieAnimation(composition, progress, modifier = Modifier.size(300.dp))
    }
}


enum class AnimationType {
    Correct,
    Wrong,
    Idle
}
