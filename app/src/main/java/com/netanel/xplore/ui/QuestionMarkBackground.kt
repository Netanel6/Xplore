package com.netanel.xplore.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.netanel.xplore.R
import com.netanel.xplore.ui.theme.SoftWhite
import kotlin.random.Random

@Composable
fun QuestionMarkBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        val numQuestionMarks = 20
        val random = remember { Random }

        repeat(numQuestionMarks) {
            val xOffset = remember { random.nextInt(-200, 200).dp }
            val yOffset = remember { random.nextInt(-100, 100).dp }
            val rotation = remember { random.nextInt(-45, 45).toFloat() }
            val alpha = remember { random.nextFloat() * 0.3f + 0.05f }
            val scale = remember { random.nextFloat() * 0.5f + 0.3f }

            Icon(
                painter = painterResource(R.drawable.question_mark),
                contentDescription = "Watermark",
                tint = SoftWhite.copy(alpha = alpha),
                modifier = Modifier
                    .size(48.dp)
                    .rotate(rotation)
                    .offset(x = xOffset, y = yOffset)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .align(Alignment.Center)

            )
        }
    }
}