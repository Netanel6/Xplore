package com.netanel.xplore.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.netanel.xplore.R
import com.netanel.xplore.home.ui.composables.QuizList
import com.netanel.xplore.localDatabase.user.viewModel.UserViewModel
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.ui.theme.SoftWhite
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    userId: String,
    onQuizSelected: (Quiz) -> Unit,
    onLogoutClicked: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val quizList by homeViewModel.quizList.collectAsState()
    val username by userViewModel.username.collectAsState()
    var showQuizList by remember { mutableStateOf(false) }
    var isUiVisible by remember { mutableStateOf(false) }

    // Fetch quizzes for user when screen loads
    LaunchedEffect(userId) {
        homeViewModel.fetchUserQuizzes(userId)
        delay(300)
        isUiVisible = true
    }


    val bgLottieComposition by rememberLottieComposition(LottieCompositionSpec.Asset("home_bg.json"))
    val mainLottieComposition by rememberLottieComposition(LottieCompositionSpec.Asset("home_animation.json"))

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.background
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        // Lottie Animation as Background (Optional - Use if you have a suitable animation)
        bgLottieComposition?.let {
            LottieAnimation(
                composition = it,
                iterations = 1,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Welcome Text, make it fade in when username is available
            AnimatedVisibility(
                visible = isUiVisible && username.isNotBlank(),
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
                exit = fadeOut(animationSpec = tween(durationMillis = 500))
            ) {
                Text(
                    text = stringResource(R.string.welcome_back, username),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(top = 48.dp, bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
            }


            Spacer(modifier = Modifier.height(48.dp))

            // Main Animation
            mainLottieComposition?.let {
                AnimatedVisibility(visible = !showQuizList, content = {
                    LottieAnimation(
                        composition = it,
                        iterations = 1,
                        modifier = Modifier
                            .height(260.dp)
                            .width(260.dp)
                    )
                })
            }

            // Quiz List (Animated appearance, slide up/down)  Place this *above* buttons.
            val density2 = LocalDensity.current
            AnimatedVisibility(
                visible = showQuizList,
                enter = slideInVertically(animationSpec = tween(durationMillis = 700)) {
                    with(density2) { -100.dp.roundToPx() }
                } + fadeIn(animationSpec = tween(700)),
                exit = slideOutVertically(animationSpec = tween(durationMillis = 700)) {
                    with(density2) { -100.dp.roundToPx() }
                } + fadeOut(animationSpec = tween(700)),
                modifier = Modifier.fillMaxWidth()

            ) {
                QuizList(
                    quizzes = quizList.orEmpty(),
                    onQuizSelected = { quiz ->
                        showQuizList = false
                        onQuizSelected(quiz)
                    },
                    onClose = {
                        showQuizList = false
                    }
                )
            }


            // Container for Buttons with Shared Animation - Slide Up/Down, plus overall Fade
            val density = LocalDensity.current
            AnimatedVisibility(
                visible = !showQuizList,
                enter = fadeIn(animationSpec = tween(durationMillis = 700)) +
                        slideInVertically(animationSpec = tween(durationMillis = 700)) {
                            with(density) { 100.dp.roundToPx() }
                        },
                exit = fadeOut(animationSpec = tween(durationMillis = 700)) +
                        slideOutVertically(animationSpec = tween(durationMillis = 700)) {
                            with(density) { 100.dp.roundToPx() }
                        }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Select Quiz Button
                    OutlinedButton(
                        onClick = { showQuizList = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SoftWhite
                        )
                    ) {
                        Text(stringResource(R.string.select_quiz), fontSize = 18.sp)
                    }

                    // Logout Button
                    OutlinedButton(
                        onClick = { onLogoutClicked() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SoftWhite
                        )
                    ) {
                        Text(stringResource(R.string.logout), fontSize = 18.sp)
                    }
                }
            }
        }
    }
}