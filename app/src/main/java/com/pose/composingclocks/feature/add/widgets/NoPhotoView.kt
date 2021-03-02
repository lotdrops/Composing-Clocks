package com.pose.composingclocks.feature.add.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NoPhotoView(modifier: Modifier = Modifier, loading: Boolean = false, isError: Boolean = false) {
    val primaryColor = MaterialTheme.colors.primary
    val centerColor = primaryColor.copy(alpha = 0.1f)
    val borderColor = primaryColor.copy(alpha = 0.25f)
    val defaultIconAlpha = 0.25f
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(shape)
            .background(centerColor)
            .border(4.dp, borderColor, shape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .fillMaxHeight(0.4f)
                .align(Alignment.Center)
        ) {
            val transition = rememberInfiniteTransition()
            val animatedAlpha by transition.animateFloat(
                initialValue = 0.15f,
                targetValue = 0.4f,
                animationSpec = infiniteRepeatable(
                    repeatMode = RepeatMode.Reverse,
                    animation = tween(
                        durationMillis = 700,
                        easing = LinearEasing
                    )
                )
            )
            val imageAlpha = if (loading) animatedAlpha else defaultIconAlpha
            Image(
                imageVector = if (isError) Icons.Filled.Close else Icons.Filled.AddCircle,
                contentDescription = null,
                colorFilter = ColorFilter.tint(primaryColor.copy(alpha = imageAlpha)),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoPhotoViewPreview() {
    NoPhotoView(loading = true)
}
