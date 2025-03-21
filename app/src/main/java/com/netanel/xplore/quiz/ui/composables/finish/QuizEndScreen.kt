package com.netanel.xplore.quiz.ui.composables.finish

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.netanel.xplore.R
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.quiz.ui.QuizResult
import com.netanel.xplore.quiz.ui.QuizViewModel
import com.netanel.xplore.ui.AnimatedComposable
import com.netanel.xplore.ui.theme.OnPrimary
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun QuizEndScreen(
    quiz: Quiz,
    quizResult: QuizResult,
    quizViewModel: QuizViewModel,
    onTryAgain: () -> Unit,
    onGoHome: () -> Unit
) {
    var isConfettiVisible by remember { mutableStateOf(true) }
    var isUiVisible by remember { mutableStateOf(false) }
    var isScoreBoardVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        quizViewModel.updateQuiz()
        delay(3000)
        isConfettiVisible = false
        isUiVisible = true
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 🎊 Confetti
        AnimatedComposable(
            isVisible = isConfettiVisible,
            content = {
                KonfettiView(
                    modifier = Modifier.fillMaxSize(),
                    parties = listOf(
                        Party(
                            speed = 8f,
                            maxSpeed = 20f,
                            damping = 0.9f,
                            spread = 360,
                            timeToLive = 3000L,
                            position = Position.Relative(0.5, 0.2),
                            emitter = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(50)
                        )
                    )
                )
            }
        )

        AnimatedComposable(
            isVisible = isUiVisible,
            enter = fadeIn(animationSpec = tween(1000)) + scaleIn(animationSpec = tween(700)),
            content = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = stringResource(R.string.quiz_completed),
                        tint = OnPrimary,
                        modifier = Modifier.size(100.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = stringResource(R.string.quiz_completed),
                        fontSize = 30.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .shadow(8.dp, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text(
                            text = stringResource(R.string.final_score, quizResult.totalScore),
                            fontSize = 24.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                            color = OnPrimary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = onTryAgain,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = stringResource(R.string.try_again), fontSize = 18.sp)
                        }

                        Button(
                            onClick = onGoHome,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = stringResource(R.string.go_home), fontSize = 18.sp)
                        }
                    }

                    // 🧮 Scoreboard Button (spread across full width)
                    Button(
                        onClick = { isScoreBoardVisible = !isScoreBoardVisible },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(text = stringResource(R.string.scoreboard), fontSize = 18.sp)
                    }

                    // 🧾 ScoreBoard List (Conditional)
                    if (isScoreBoardVisible) {
                        ScoreBoardList() {
                            isScoreBoardVisible = false
                        }
                    }
                }
            }
        )
    }
}
