package com.netanel.xplore.home

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.R
import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.localDatabase.user.viewModel.UserViewModel
import com.netanel.xplore.ui.AnimatedComposable
import com.netanel.xplore.ui.theme.BluePrimary
import com.netanel.xplore.ui.theme.OnPrimary
import com.netanel.xplore.ui.theme.PurpleGrey40
import com.netanel.xplore.ui.theme.Purple40
import com.netanel.xplore.ui.theme.White

@Composable
fun HomeScreen(
    userId: String,
    onQuizSelected: (String) -> Unit,
    onLogoutClicked: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val quizList by homeViewModel.quizList.collectAsState()
    var showQuizList by remember { mutableStateOf(false) }
    val username by userViewModel.username.collectAsState(initial = "")

    LaunchedEffect(userId) {
        homeViewModel.fetchQuizzes(userId)
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
            // Title Section
            Column(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.question_mark),
                    contentDescription = "Question mark",
                    tint = BluePrimary,
                    modifier = Modifier.size(44.dp)
                )
                Text(
                    text = "ברוך הבא, $username",
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = BluePrimary
                )
            }

            // "Select Quiz" Button
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
                            containerColor = BluePrimary,
                            contentColor = OnPrimary
                        )
                    ) {
                        Text(stringResource(R.string.select_quiz))
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            // "Logout" Button
            AnimatedComposable(
                isVisible = !showQuizList,
                enter = expandVertically(animationSpec = tween(500)),
                exit = shrinkVertically(animationSpec = tween(500)),
                content = {
                    Button(
                        onClick = {
                            userViewModel.logout()
                            onLogoutClicked()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Purple40,
                            contentColor = OnPrimary
                        )
                    ) {
                        Text(stringResource(R.string.logout))
                    }
                }
            )

            // Quiz List Section
            AnimatedComposable(
                isVisible = showQuizList,
                enter = expandVertically(animationSpec = tween(500)),
                exit = shrinkVertically(animationSpec = tween(500)),
                content = {
                    QuizList(
                        quizzes = quizList.orEmpty(),
                        onQuizSelected = { quizId ->
                            showQuizList = false
                            onQuizSelected(quizId)
                        },
                        onClose = { showQuizList = false }
                    )
                }
            )
        }
    }
}

@Composable
fun QuizList(
    quizzes: List<User.Quiz>,
    onQuizSelected: (String) -> Unit,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(id = R.string.select_quiz_title),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = BluePrimary
                )
                IconButton(onClick = onClose) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                        contentDescription = "Close",
                        tint = PurpleGrey40
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
            ) {
                items(quizzes) { quiz ->
                    QuizListItem(
                        quiz = quiz,
                        onClick = { onQuizSelected(quiz.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun QuizListItem(quiz: User.Quiz, onClick: () -> Unit) {
    var isHovered by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        awaitFirstDown().also {
                            isHovered = true
                        }
                        val up = awaitPointerEvent(PointerEventPass.Final)
                        if (up.changes.any { it.changedToUp() }) {
                            isHovered = false
                        }
                    }
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHovered) 4.dp else 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.exclamation_mark),
                contentDescription = "Quiz icon",
                tint = PurpleGrey40,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = quiz.title,
                style = MaterialTheme.typography.bodyLarge.copy(color = BluePrimary)
            )
        }
    }
}
