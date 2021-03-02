package com.pose.composingclocks.feature.config

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.pose.composingclocks.R
import com.pose.composingclocks.app.LANDSCAPE_SIZE_THRESHOLD
import com.pose.composingclocks.common.widgets.SectionTitle

private val languages = listOf("English", "Español")

@Composable
fun LanguagePicker(selectedLanguage: Int, onLanguageSelected: (Int) -> Unit) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE &&
        configuration.screenWidthDp > LANDSCAPE_SIZE_THRESHOLD

    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = 0.dp) {
        if (isLandscape) LanguageContentLandscape(selectedLanguage, onLanguageSelected)
        else LanguageContentPortrait(selectedLanguage, onLanguageSelected)
    }
}

@Composable
fun LanguageContentPortrait(selectedLanguage: Int, onLanguageSelected: (Int) -> Unit) {
    Column(Modifier.padding(16.dp)) {
        SectionTitle(stringResource(id = R.string.config_screen_language_title))
        Spacer(modifier = Modifier.height(16.dp))
        ToggleGroup(selectedLanguage, onLanguageSelected)
    }
}

@Composable
fun LanguageContentLandscape(selectedLanguage: Int, onLanguageSelected: (Int) -> Unit) {
    Row(Modifier.padding(16.dp)) {
        SectionTitle(
            stringResource(id = R.string.config_screen_language_title),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
        )
        ToggleGroup(selectedLanguage, onLanguageSelected)
    }
}

@Composable
private fun ToggleGroup(selectedPosition: Int, onClick: (Int) -> Unit) {
    val shape = RoundedCornerShape(4.dp)
    Row(
        Modifier
            .padding(vertical = 8.dp)
            .clip(shape)
            .border(1.dp, Color(0xFFAAAAAA), shape)
    ) {
        languages.forEachIndexed { index, element ->

            val verticalPadding = if (index == selectedPosition) 8.dp else 0.dp
            Text(
                text = element,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .run {
                        if (index != selectedPosition) this
                        else background(Color(0xFFDDDDDD)).border(1.dp, Color.Gray)
                    }
                    .clickable(
                        onClick = { onClick(index) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp, vertical = verticalPadding)
            )
        }
    }
}
