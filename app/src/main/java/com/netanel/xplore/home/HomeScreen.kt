package com.netanel.xplore.home


import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
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
import com.netanel.xplore.ui.theme.Pink40

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
                Text(
                    text = "ברוך הבא, $username",
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = MaterialTheme.colorScheme.primary
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
                            onLogoutClicked()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Pink40,
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
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onClose) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Divider(color = Color.White, thickness = 1.dp)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
            ) {
                items(quizzes.size, key = { it }) { index ->
                    val quiz = quizzes[index]
                    QuizListItem(
                        quiz = quiz,
                        onClick = { onQuizSelected(quiz.id) },
                        backgroundColor = if (index % 2 == 0) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun QuizListItem(quiz: User.Quiz, onClick: () -> Unit, backgroundColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.question_mark),
                contentDescription = "Quiz icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = quiz.title,
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}