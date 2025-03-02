package com.netanel.xplore.home.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.R
import com.netanel.xplore.localDatabase.user.viewModel.UserViewModel
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.home.ui.composables.QuizList

import com.netanel.xplore.ui.AnimatedComposable
import com.netanel.xplore.ui.theme.OnPrimary
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
    val username by userViewModel.username.collectAsState(initial = "")
    var showQuizList by remember { mutableStateOf(false) }
    var isUiVisible by remember { mutableStateOf(false) }

    // 🌟 Fetch quizzes for user when screen loads
    LaunchedEffect(userId) {
        homeViewModel.fetchUserQuizzes(userId)
        delay(300)
        isUiVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Welcome Text
            AnimatedComposable(
                isVisible = isUiVisible,
                enter = fadeIn(animationSpec = tween(1000)),
                content = {
                    Text(
                        text = stringResource(R.string.welcome_back, username),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🏆 Select Quiz Button
            AnimatedComposable(
                isVisible = !showQuizList,
                enter = expandVertically(animationSpec = tween(500)),
                exit = shrinkVertically(animationSpec = tween(500)),
                content = {
                    Button(
                        onClick = { showQuizList = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OnPrimary,
                            contentColor = SoftWhite
                        )
                    ) {
                        Text(stringResource(R.string.select_quiz))
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            // 🚪 Logout Button
            AnimatedComposable(
                isVisible = !showQuizList,
                enter = expandVertically(animationSpec = tween(500)),
                exit = shrinkVertically(animationSpec = tween(500)),
                content = {
                    Button(
                        onClick = { onLogoutClicked() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OnPrimary,
                            contentColor = SoftWhite
                        )
                    ) {
                        Text(stringResource(R.string.logout))
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            // 🎮 Quiz List
            AnimatedComposable(
                isVisible = showQuizList,
                enter = expandVertically(animationSpec = tween(500)),
                exit = shrinkVertically(animationSpec = tween(500)),
                content = {
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
            )
        }
    }
}
