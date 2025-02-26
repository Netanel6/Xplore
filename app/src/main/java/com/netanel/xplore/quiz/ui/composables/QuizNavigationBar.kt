package com.netanel.xplore.quiz.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    isNextEnabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🔹 Previous Button
        IconButton(onClick = { onPreviousClicked() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.previous_question),
                tint = OnPrimary
            )
        }

        // 🔹 Quiz Timer Placeholder
        Text(
            text = stringResource(R.string.quiz_timer),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )

        // 🔹 Submit / Next Button
        Button(
            onClick = { onNextClicked() },
            colors = ButtonDefaults.buttonColors(containerColor = OnPrimary),
            enabled = isNextEnabled
        ) {
            Text(stringResource(R.string.submit), color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
