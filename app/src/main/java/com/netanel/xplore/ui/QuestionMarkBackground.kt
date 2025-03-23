package com.netanel.xplore.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.netanel.xplore.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun QuestionMarkBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        val numQuestionMarks = 120
        val random = remember { Random }
        val density = LocalDensity.current
        val screenWidthPx =
            with(density) { LocalContext.current.resources.displayMetrics.widthPixels.dp.toPx() } // Get screen width
        val screenHeightPx =
            with(density) { LocalContext.current.resources.displayMetrics.heightPixels.dp.toPx() } // Get screen height

        val scales = remember { List(numQuestionMarks) { Animatable(0f) } }
        val rotations = remember { List(numQuestionMarks) { Animatable(0f) } }

        LaunchedEffect(Unit) {
            scales.forEachIndexed { index, scale ->
                launch {
                    delay(index * 50L)
                    scale.animateTo(
                        1f,
                        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
                    )
                }
            }
            rotations.forEachIndexed { index, rotation ->
                launch {
                    delay(index * 50L)
                    rotation.animateTo(
                        360f,
                        animationSpec = tween(durationMillis = 3000)
                    )
                }
            }
        }


        repeat(numQuestionMarks) { index ->
            val angle = remember { 2f * Math.PI.toFloat() * random.nextFloat() }
            val distance = remember { random.nextFloat() * 0.5f + 0.1f }
            val xOffset = remember { (distance * screenWidthPx * 0.5f * cos(angle)) }
            val yOffset = remember { (distance * screenHeightPx * 0.5f * sin(angle)) }
            val initialRotation = remember { random.nextInt(-45, 45).toFloat() }
            val alpha = remember { random.nextFloat() * 0.3f + 0.05f }

            Icon(
                painter = painterResource(R.drawable.question_mark),
                contentDescription = "Watermark",
                tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = alpha),
                modifier = Modifier
                    .size(48.dp)
                    .graphicsLayer {
                        scaleX = scales[index].value
                        scaleY = scales[index].value

                        rotationZ = initialRotation + rotations[index].value
                    }
                    .align(Alignment.Center)
                    .offset(x = xOffset.dp, y = yOffset.dp)

            )
        }
    }
}