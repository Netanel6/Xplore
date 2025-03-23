package com.netanel.xplore.quiz.ui.composables.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.netanel.xplore.R
import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.ui.theme.Easy
import com.netanel.xplore.ui.theme.Hard
import com.netanel.xplore.ui.theme.Medium

@Composable
fun DifficultyBar(difficulty: Question.DifficultyLevel) {
    val (color, _) = when (difficulty) {
        Question.DifficultyLevel.Easy -> Pair(Easy, stringResource(R.string.easy))
        Question.DifficultyLevel.Medium -> Pair(Medium, stringResource(R.string.medium))
        Question.DifficultyLevel.Hard -> Pair(Hard, stringResource(R.string.hard))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.difficulty),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box(
            modifier = Modifier
                .height(12.dp)
                .width(40.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color)
        )
    }
}