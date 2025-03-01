package com.netanel.xplore.home

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.R
import com.netanel.xplore.localDatabase.user.viewModel.UserViewModel
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.ui.AnimatedComposable
import com.netanel.xplore.ui.theme.OnPrimary
import com.netanel.xplore.ui.theme.SoftWhite
import com.netanel.xplore.utils.formatTime
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    userId: String,
    onQuizSelected: (String) -> Unit,
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
    quizzes: List<Quiz>,
    onQuizSelected: (String) -> Unit,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
                    color = OnPrimary
                )
                IconButton(onClick = onClose) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                        contentDescription = "Close",
                        tint = OnPrimary
                    )
                }
            }

            Divider(color = MaterialTheme.colorScheme.onPrimary, thickness = 1.dp)

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
            ) {
                items(quizzes.size, key = { it }) { index ->
                    val quiz = quizzes[index]
                    QuizListItem(
                        quiz = quiz,
                        onClick = { onQuizSelected(quiz._id) },
                        backgroundColor = if (index % 2 == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun QuizListItem(
    quiz: Quiz,
    onClick: () -> Unit,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 🏷 Quiz Title
            Text(
                text = quiz.title ?: "חידון ללא שם",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = OnPrimary
                )
            )

            // 🔢 Number of Questions
            Text(
                text = "שאלות: ${quiz.questions.size}",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            // ⏳ Quiz Timer
            if (quiz.quizTimer > 0) {
                Text(
                    text = "⏳ זמן חידון: ${quiz.quizTimer.formatTime()}",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }

            // 🔒 Answer Lock Timer
            if (quiz.answerLockTimer > 0) {
                Text(
                    text = "🔒 זמן נעילת תשובות: ${quiz.answerLockTimer.formatTime()}",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }

            // ✏️ Quiz Creator (if available)
            quiz.creatorId?.let {
                Text(
                    text = "✍️ נוצר ע\"י: $it",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }
    }
}
