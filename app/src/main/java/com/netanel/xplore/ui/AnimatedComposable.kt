package com.netanel.xplore.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable

@Composable
fun AnimatedComposable(
    content: @Composable () -> Unit,
    isVisible: Boolean,
    animationDuration: Int = 1500,
    enter: EnterTransition = fadeIn(animationSpec = tween(animationDuration)),
    exit: ExitTransition = fadeOut(animationSpec = tween(animationDuration))
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = enter,
        exit = exit
    ) {
        content()
    }
}