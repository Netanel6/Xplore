package com.netanel.xplore.quiz.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.netanel.xplore.R
import com.netanel.xplore.ui.theme.OnPrimary
import kotlinx.coroutines.delay

@Composable
fun QuizFinishedAnimation(onAnimationEnd: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000L)
        onAnimationEnd()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.times_up),
            style = MaterialTheme.typography.headlineLarge,
            color = OnPrimary
        )
    }
}
