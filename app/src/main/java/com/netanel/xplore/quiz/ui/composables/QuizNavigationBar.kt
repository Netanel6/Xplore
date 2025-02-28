package com.netanel.xplore.quiz.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.netanel.xplore.R
import com.netanel.xplore.ui.theme.OnPrimary

@Composable
fun QuizNavigationBar(
    onPreviousClicked: () -> Unit,
    onNextClicked: () -> Unit,
    isNextEnabled: Boolean,
    isPreviousEnabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            QuizNavButtonType.Previous to isPreviousEnabled,
            QuizNavButtonType.Next to isNextEnabled
        ).forEach { (buttonType, isEnabled) ->
            Button(
                onClick = {
                    when (buttonType) {
                        QuizNavButtonType.Previous -> onPreviousClicked()
                        QuizNavButtonType.Next -> onNextClicked()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = OnPrimary),
                enabled = isEnabled,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(buttonType.labelRes),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

sealed class QuizNavButtonType(val labelRes: Int) {
    data object Previous : QuizNavButtonType(R.string.previous_question)
    data object Next : QuizNavButtonType(R.string.submit)
}
