package com.netanel.xplore.home

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
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

@Composable
fun HomeScreen(
    userId: String,
    onQuizSelected: (String) -> Unit,
    onLogoutClicked: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val quizList by viewModel.quizList.collectAsState()
    var showQuizList by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        viewModel.fetchQuizzes(userId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4285F4))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // "Xplore" title with question mark on top
            Column(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.question_mark),
                    contentDescription = "Question mark",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
                Text(
                    stringResource(R.string.app_name),
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp,
                    color = Color.White
                )
            }

            // "Select Quiz" button with shrink/expand animation
            AnimatedComposable(
                isVisible = !showQuizList,
                enter = expandVertically(animationSpec = tween(500), expandFrom = Alignment.Top),
                exit = shrinkVertically(animationSpec = tween(500), shrinkTowards = Alignment.Bottom),
                content = {
                    Button(
                        onClick = { showQuizList = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF4285F4)
                        )
                    ) {
                        Text(stringResource(R.string.select_quiz), fontSize = 20.sp)
                    }
                }
            )

            Spacer(Modifier.size(width = 2.dp, height = 20.dp))

            // "Logout" button with shrink/expand animation
            AnimatedComposable(
                isVisible = !showQuizList,
                enter = expandVertically(animationSpec = tween(500), expandFrom = Alignment.Top),
                exit = shrinkVertically(animationSpec = tween(500), shrinkTowards = Alignment.Bottom),
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
                            containerColor = Color.White,
                            contentColor = Color(0xFF4285F4)
                        )
                    ) {
                        Text(stringResource(R.string.logout), fontSize = 20.sp)
                    }
                }
            )
            /*Fade in out anim
            * enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(300)),
                * */
            // Quiz list section
            AnimatedComposable(
                isVisible = showQuizList,
                enter = expandVertically(animationSpec = tween(300), expandFrom = Alignment.Top),
                exit = shrinkVertically(animationSpec = tween(300), shrinkTowards = Alignment.Bottom),
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
            containerColor = Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
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
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = onClose) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                        contentDescription = "Close"
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
                painter = painterResource(id = R.drawable.exclamation_mark), // Replace with dynamic drawable
                contentDescription = "Quiz icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = quiz.title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}