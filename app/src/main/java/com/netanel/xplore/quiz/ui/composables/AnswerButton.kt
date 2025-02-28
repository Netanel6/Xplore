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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.netanel.xplore.ui.theme.AnswerBorder
import com.netanel.xplore.ui.theme.AnswerUnselected
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
        isSelected -> AnswerUnselected
        else -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when {
        isSelected -> SoftWhite
        else -> OnPrimary
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
        border = if (!isSelected) BorderStroke(2.dp, AnswerBorder) else BorderStroke(2.dp, OnPrimary),
        enabled = !isLocked
    ) {
        Text(
            text = answer,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = if (isSelected) SoftWhite else OnPrimary
        )
    }
}
