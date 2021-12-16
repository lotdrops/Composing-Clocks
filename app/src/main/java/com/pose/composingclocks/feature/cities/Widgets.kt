package com.pose.composingclocks.feature.cities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.pose.composingclocks.app.theme.typography
import com.pose.composingclocks.common.widgets.NoPhoto

@Composable
fun TitleWithGradient(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(96.dp)
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
    ) {
        Text(
            text = text,
            style = typography.h5,
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .align(Alignment.BottomStart),
        )
    }
}

@Composable
fun CityImage(url: String, description: String, modifier: Modifier = Modifier) {
    val painter = rememberImagePainter(url)
    Box {
        Image(
            painter = painter,
            contentDescription = description,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        if (painter.state is ImagePainter.State.Error) { NoPhoto() }
    }
}
