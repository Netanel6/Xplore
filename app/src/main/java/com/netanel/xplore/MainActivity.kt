package com.netanel.xplore

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

import com.netanel.xplore.temp.model.Question
import com.netanel.xplore.temp.repository.QuizRepository
import com.netanel.xplore.temp.ui.QuizViewModel
import com.netanel.xplore.ui.theme.XploreTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XploreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    QuizScreen()
                }
            }
        }
    }
}

@Composable
fun QuizScreen(viewModel: QuizViewModel = hiltViewModel()) {
    val questions by viewModel.questions.collectAsState()
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var showAnimation by remember { mutableStateOf(false) }

    if (showAnimation) {
        LottieAnimationScreen(onAnimationEnd = {
            showAnimation = false
            currentQuestionIndex += 1
        })
    } else {
        if (currentQuestionIndex < questions.size) {
            QuizQuestion(
                question = questions[currentQuestionIndex],
                onAnswerSelected = { isCorrect ->
                    if (isCorrect) {
                        showAnimation = true
                    }
                }
            )
        } else {
            // Display end of quiz or summary screen
            Text(
                text = "You've completed the quiz!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun QuizQuestion(question: Question, onAnswerSelected: (Boolean) -> Unit) {
    // Randomize the answers while keeping track of the correct one
    val shuffledAnswers = remember {
        val answersWithIndex = question.answers.mapIndexed { index, answer ->
            index to answer
        }.shuffled()
        answersWithIndex
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = question.text, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        shuffledAnswers.forEachIndexed { _, (originalIndex, answer) ->
            ClickableText(
                text = AnnotatedString(answer),
                modifier = Modifier.padding(8.dp),
                onClick = {
                    // Check if the selected answer is correct
                    val isCorrect = originalIndex == question.correctAnswerIndex
                    onAnswerSelected(isCorrect)
                }
            )
        }
    }
}

@Composable
fun LottieAnimationScreen(onAnimationEnd: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lottie_animation.json"))
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
            .padding(16.dp)
    ) {
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.fillMaxSize()
        )
    }
}
