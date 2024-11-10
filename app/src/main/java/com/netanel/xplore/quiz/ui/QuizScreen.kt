package com.netanel.xplore.quiz.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

    // Track answered questions that should be locked when revisited
    val lockedQuestions = remember { mutableStateListOf<Int>() }
    // Track questions that have already shown the animation
    val animatedQuestions = remember { mutableStateListOf<Int>() }

    // Store the selected answers for each question with mutable state wrappers
    val selectedAnswers = remember { MutableList(questions.size) { mutableStateOf<Int?>(null) } }

    if (showAnimation) {
        LottieAnimationScreen(
            animationType = animationType,
            onAnimationEnd = {
                showAnimation = false
            }
        )
    }

    if (currentQuestionIndex < questions.size) {
        // Check if the current question should be locked based on previous answers
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

                    // Show animation only if this question has not been animated before
                    if (currentQuestionIndex !in animatedQuestions) {
                        showAnimation = true
                        animatedQuestions.add(currentQuestionIndex) // Mark this question as animated
                    }

                    // Lock answer selection for this question
                    if (currentQuestionIndex !in lockedQuestions) {
                        lockedQuestions.add(currentQuestionIndex)
                    }

                    // Move to the next question after handling animation
                    currentQuestionIndex += 1
                }
            },
            onPreviousClicked = {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex -= 1
                }
            }
        )
    } else {
        // Display end of quiz or summary screen
        Text(
            text = "סיימת את השאלון! גש לקבל סוכריה",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
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
    // Create a shuffled set of answers for each question separately
    val shuffledAnswers = remember(currentQuestionNumber) {
        val answersWithIndex = question.answers.mapIndexed { index, answer ->
            index to answer
        }.shuffled()
        answersWithIndex
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar with question number and progress indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentQuestionNumber > 1) {
                Text(
                    text = "לשאלה הקודמת",
                    color = Color(0xFF1B5E20),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onPreviousClicked() }
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }
            Text(
                text = "$currentQuestionNumber/$totalQuestions",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
        // Question text
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
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

        // List of answers
        Column(modifier = Modifier.fillMaxWidth()) {
            shuffledAnswers.forEachIndexed { index, (originalIndex, answer) ->
                val isSelected = userSelectedAnswer == originalIndex
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isAnswerLocked) { onAnswerSelected(originalIndex) },
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(
                            0xFFB2DFDB
                        ) else Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = null // RadioButton is displayed but not clickable independently
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = answer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next button
        Button(
            onClick = { onNextClicked() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004D40))
        ) {
            Text(
                text = "לשאלה הבאה",
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun LottieAnimationScreen(animationType: AnimationType, onAnimationEnd: () -> Unit) {
    val animationFile = if (animationType == AnimationType.Correct) "black_cat_2.json" else "black_cat_1.json"

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(animationFile))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        speed = 1f
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            onAnimationEnd()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.fillMaxSize(0.8f)
        )
    }
}

enum class AnimationType{
    Correct,
    Wrong,
    Idle
}
