package com.netanel.xplore

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.os.LocaleList
import android.view.View
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.FirebaseAuth
import com.netanel.xplore.MainActivity.Companion.firebaseAuth
import com.netanel.xplore.auth.ui.AuthScreen

import com.netanel.xplore.temp.model.Question
import com.netanel.xplore.temp.repository.QuizRepository
import com.netanel.xplore.temp.ui.QuizViewModel
import com.netanel.xplore.ui.theme.XploreTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        val firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configuration = Configuration(resources.configuration)
        configuration.setLayoutDirection(Locale("he"))  // Hebrew as an example
        resources.updateConfiguration(configuration, resources.displayMetrics)

        enableEdgeToEdge()
        setContent {
            XploreTheme {
                MainScreen()
            }
        }
    }
}

//Main Container
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    Scaffold(
        topBar = { MainTopAppBar() },
    ) {
        AuthScreen(auth = firebaseAuth)
//        QuizScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar() {
    TopAppBar(
        title = { Text("Main Screen") },
        navigationIcon = { Icon(Icons.Default.Menu, contentDescription = "Menu Icon") }
    )
}


//Quiz Screen
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
    // TODO: Add Lottie roatation and randomized
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("black_cat_2.json"))
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
