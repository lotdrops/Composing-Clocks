package com.pose.composingclocks.common.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pose.composingclocks.R
import com.pose.composingclocks.app.theme.typography

@Composable
fun ScreenTitleWithBack(text: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 16.dp)) {
        ScreenBack(onBack = onBack)
        ScreenTitle(text = text, modifier, hasBack = true)
    }
}

@Composable
fun ScreenBack(onBack: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onBack, modifier) {
        Icon(
            Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.action_up)
        )
    }
}

@Composable
fun ScreenTitle(text: String, modifier: Modifier = Modifier, hasBack: Boolean = false) {
    val textModifier = if (hasBack) modifier else modifier.padding(top = 16.dp, bottom = 8.dp)
    Text(
        text = text,
        style = typography.h4,
        modifier = textModifier,
    )
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = typography.h5,
        modifier = modifier,
    )
}
