package org.eson.liteble.main.composable

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.runtime.Composable

@Composable
fun DetailScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Filled.Backup, contentDescription = "返回")
                    }
                },
                title = {
                    Text(text = "设备详情")
                })
        }
    ) {

    }
}