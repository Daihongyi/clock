package com.Ayin.clock.util

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

@OptIn(ExperimentalAnimationApi::class)
fun slideAnimation(direction: Int): ContentTransform {
    return slideInHorizontally(
        animationSpec = tween(durationMillis = 300),
        initialOffsetX = { fullWidth -> direction * fullWidth }
    ) + fadeIn(animationSpec = tween(300)) togetherWith
            slideOutHorizontally(
                animationSpec = tween(durationMillis = 300),
                targetOffsetX = { fullWidth -> -direction * fullWidth }
            ) + fadeOut(animationSpec = tween(300))
}