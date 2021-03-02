package com.pose.composingclocks.feature.add.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pose.composingclocks.R

@Composable
fun TimeZoneDropDown(
    timezones: List<String>,
    selectedTimezone: Int,
    onTimezoneSelected: (Int) -> Unit,
) {
    Row {
        Text(
            text = stringResource(id = R.string.add_screen_time_difference),
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(16.dp))

        DropDown(
            items = timezones,
            selected = selectedTimezone,
            onSelect = onTimezoneSelected,
            modifier = Modifier.wrapContentWidth()
        )
    }
}

@Composable
private fun DropDown(
    items: List<String>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Text(
            text = items[selected],
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = { showMenu = true })
                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
        ) {
            items.forEachIndexed { index, itemText ->
                DropdownMenuItem(
                    onClick = {
                        onSelect(index)
                        showMenu = false
                    }
                ) {
                    Text(text = itemText)
                }
            }
        }
    }
}
