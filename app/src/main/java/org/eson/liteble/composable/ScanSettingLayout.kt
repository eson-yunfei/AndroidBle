package org.eson.liteble.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eson.liteble.data.AppCommonData
import org.eson.liteble.data.SortType

@Composable
fun ScanSettingLayout() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color(0xbbf5f5f5))
            .padding(16.dp, 10.dp)
    ) {

        Text(text = "Sort options")
        Row {
            RadioButton(selected = AppCommonData.rssiSortType.value == SortType.NONE, onClick = {
                AppCommonData.rssiSortType.value = SortType.NONE
            })
            Text(text = "None", modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.weight(1f))
            RadioButton(selected = AppCommonData.rssiSortType.value == SortType.ASC, onClick = {
                AppCommonData.rssiSortType.value = SortType.ASC
            })
            Text(text = "ASC", modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.weight(1f))

            RadioButton(selected = AppCommonData.rssiSortType.value == SortType.DESC, onClick = {
                AppCommonData.rssiSortType.value = SortType.DESC
            })
            Text(text = "DESC", modifier = Modifier.align(Alignment.CenterVertically))
        }

        Text(text = "Filtering options")
        Row {
            Checkbox(checked = AppCommonData.hideNoNameState.value, onCheckedChange = {
                AppCommonData.hideNoNameState.value = it
            })
            Text(text = "Hide no name", modifier = Modifier.align(Alignment.CenterVertically))

            Spacer(modifier = Modifier.weight(1f))
            Checkbox(checked = AppCommonData.hideLowRssiState.value, onCheckedChange = {
                AppCommonData.hideLowRssiState.value = it
            })
            Text(text = "Hide low rssi", modifier = Modifier.align(Alignment.CenterVertically))
        }

    }
}


@Preview
@Composable
fun PreviewScanSetting() {
    MaterialTheme {
        ScanSettingLayout()
    }
}