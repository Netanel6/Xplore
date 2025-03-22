package com.netanel.xplore.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.netanel.xplore.R
import kotlin.random.Random

@Composable
fun QuestionMarkBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        val numQuestionMarks = 80
        val random = remember { Random }

        // TODO: Change the start to top right up to the lottie animation
        repeat(numQuestionMarks) {
            val xOffset = remember { random.nextFloat() * 1.0f }  // 0f to 1f
            val yOffset = remember { random.nextFloat() * 1.0f }  // 0f to 1f
            val rotation = remember { random.nextInt(-45, 45).toFloat() }
            val scale = remember { random.nextFloat() * 0.5f + 0.3f }

            Icon(
                painter = painterResource(R.drawable.question_mark),
                contentDescription = "Watermark",
                tint = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(48.dp)
                    .rotate(rotation)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .align(Alignment.TopStart)
                    .offset(
                        x = with(LocalDensity.current) { (xOffset * 360).dp },
                        y = with(LocalDensity.current) { (yOffset * 720).dp }
                    )
            )
        }
    }
}
