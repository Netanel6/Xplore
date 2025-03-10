package com.netanel.xplore.quiz.ui.composables.answer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.netanel.xplore.ui.theme.AnswerBorder
import com.netanel.xplore.ui.theme.NeutralGray
import com.netanel.xplore.ui.theme.OnPrimary
import com.netanel.xplore.ui.theme.SoftWhite

@Composable
fun AnswerButton(
    answer: String,
    isSelected: Boolean,
    isLocked: Boolean,
    onClick: () -> Unit
) {
    val containerColor = when {
        isLocked -> NeutralGray   // 🔐 Gray when Locked
        isSelected -> OnPrimary   // 🔵 Blue when Selected
        else -> SoftWhite         // ⬜ White when Available
    }

    val contentColor = when {
        isLocked -> AnswerBorder  // 🔐 Medium Gray Text when Locked
        isSelected -> SoftWhite   // 🔵 White Text when Selected
        else -> OnPrimary         // ⬜ Default Text Color
    }

    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = if (!isSelected) BorderStroke(2.dp, AnswerBorder) else BorderStroke(2.dp, SoftWhite),
        enabled = !isLocked
    ) {
        Text(
            text = answer,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor
        )
    }
}
