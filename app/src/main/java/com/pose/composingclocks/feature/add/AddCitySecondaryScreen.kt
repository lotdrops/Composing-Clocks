package com.pose.composingclocks.feature.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pose.composingclocks.R
import com.pose.composingclocks.common.widgets.ScreenTitleWithBack
import com.pose.composingclocks.common.widgets.SectionTitle
import com.pose.composingclocks.core.scopednav.navigation.NoParams
import com.pose.composingclocks.core.scopednav.navigation.ScreenDestination
import com.pose.composingclocks.feature.add.widgets.NoPhotoView
import dev.chrisbanes.accompanist.coil.CoilImage

object AddCitySecondaryScreen : ScreenDestination<NoParams>(pathRoot = "addCitySecondaryScreen")

@Composable
fun AddCitySecondaryScreen(viewModel: AddCitySecondaryViewModel) {
    val description = viewModel.description.collectAsState()
    val url = viewModel.photoUrl.collectAsState()

    AddCityOptionalContent(
        description = description.value,
        onDescriptionChange = { viewModel.description.value = it },
        url = url.value,
        onUrlChange = { viewModel.photoUrl.value = it },
        onSave = { viewModel.onSaveClicked() },
        onBack = { viewModel.onBack() },
    )
}

@Composable
private fun AddCityOptionalContent(
    description: String,
    onDescriptionChange: (String) -> Unit,
    url: String,
    onUrlChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(R.color.very_light_blue))
            .padding(vertical = 16.dp)
    ) {
        ScreenTitleWithBack(
            text = stringResource(R.string.add_screen_optional_data_title),
            onBack = onBack,
        )

        Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            TextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 8.dp),
                label = { Text(text = stringResource(id = R.string.add_screen_description_hint)) },
            )
            SectionTitle(
                text = stringResource(R.string.add_screen_image_title),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            )

            TextField(
                value = url,
                onValueChange = { onUrlChange(it) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = { Text(text = stringResource(id = R.string.add_screen_image_url)) },
            )

            PhotoSelectedView(
                url,
                Modifier
                    .weight(2f)
                    .padding(vertical = 8.dp)
            )

            Button(
                onClick = onSave,
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(id = R.string.add_screen_save_button),
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp),
                )
            }
        }
    }
}

@Composable
fun PhotoSelectedView(url: String, modifier: Modifier) {
    if (url.trim().isEmpty()) {
        NoPhotoView(modifier.padding(vertical = 8.dp))
    } else {
        CoilImage(
            data = url,
            contentDescription = null,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            error = { NoPhotoView(modifier, loading = false, isError = true) },
            loading = { NoPhotoView(modifier, true) },
        )
    }
}

@Preview
@Composable
fun AddCityOptionalContentPreview() {
    var description = ""
    var url = ""
    AddCityOptionalContent(
        description = description,
        onDescriptionChange = { description = it },
        url = url,
        onUrlChange = { url = it },
        {},
        {},
    )
}
