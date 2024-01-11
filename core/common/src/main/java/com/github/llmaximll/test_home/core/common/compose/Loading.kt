package com.github.llmaximll.test_home.core.common.compose

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
fun rememberShimmerEffect(): Color {
    val animatedValue = remember { Animatable(Color.DarkGray) }

    /*LaunchedEffect(Unit) {
        animatedValue.animateTo(
            targetValue = Color.LightGray,
            animationSpec = infiniteRepeatable(
                animation = tween(500),
                repeatMode = RepeatMode.Reverse
            )
        )
    }*/

    return animatedValue.value
}