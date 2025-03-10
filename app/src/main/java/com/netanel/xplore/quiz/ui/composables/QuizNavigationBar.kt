package com.netanel.xplore.quiz.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.netanel.xplore.R
import com.netanel.xplore.ui.theme.OnPrimary

@Composable
fun QuizNavigationBar(
    onPreviousClicked: () -> Unit,
    onNextClicked: () -> Unit,
    isNextEnabled: Boolean,
    isPreviousEnabled: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp), // 🛠 Adds padding for better UX
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), // ⬇️ Ensures buttons are NOT stuck to the screen edge
            horizontalArrangement = Arrangement.spacedBy(12.dp), // 🛠 Proper spacing between buttons
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onPreviousClicked,
                colors = ButtonDefaults.buttonColors(containerColor = OnPrimary),
                enabled = isPreviousEnabled,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp) // 📏 Ensures consistent button size
            ) {
                Text(
                    text = stringResource(R.string.previous_question),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Button(
                onClick = onNextClicked,
                colors = ButtonDefaults.buttonColors(containerColor = OnPrimary),
                enabled = isNextEnabled,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp) // 📏 Ensures consistent button size
            ) {
                Text(
                    text = stringResource(R.string.submit),
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
