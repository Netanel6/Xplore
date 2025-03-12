package com.netanel.xplore.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.netanel.xplore.R
import com.netanel.xplore.ui.theme.OnPrimary
import com.netanel.xplore.ui.theme.SoftWhite
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar() {
    val offset = remember { Animatable(-200f) }

    LaunchedEffect(Unit) {
        offset.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 800)
        )
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = OnPrimary,
            titleContentColor = SoftWhite
        ),
        title = {
            // Outer container for styling
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(OnPrimary.copy(alpha = 0.8f))
                    .shadow(4.dp, MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {

                // Question Mark Background (Multiple, Shuffled)
                QuestionMarkBackground()

                // App Name (Sliding Text)
                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier
                        .offset(x = offset.value.dp)
                        .padding(8.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,
                    color = SoftWhite
                )
            }
        }
    )
}

@Composable
fun QuestionMarkBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        val numQuestionMarks = 20
        val random = remember { Random }

        repeat(numQuestionMarks) {
            val xOffset = remember { random.nextInt(-150, 150).dp }
            val yOffset = remember { random.nextInt(-50, 50).dp }
            val rotation = remember { random.nextInt(-45, 45).toFloat() }
            val alpha = remember { random.nextFloat() * 0.1f + 0.05f }
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

@Preview
@Composable
fun MainTopAppBarPreview() {
    MainTopAppBar()
}