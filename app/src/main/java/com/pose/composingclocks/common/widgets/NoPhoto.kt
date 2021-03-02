package com.pose.composingclocks.common.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NoPhoto() {
    val iconColor = MaterialTheme.colors.primary.copy(alpha = 0.25f)
    val bgColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Image(
            imageVector = Icons.Filled.Close,
            contentDescription = null,
            colorFilter = ColorFilter.tint(iconColor),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize(0.6f)
                .padding(8.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun NoPhotoPreview() {
    NoPhoto()
}
