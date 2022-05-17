package org.eson.liteble.main.composable

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar() {
                Text(text = "Lite Ble", color = Color.White)
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "设置",
                    tint = Color.White,
                    modifier = Modifier.wrapContentSize()
//                        .align(Alignment.End)
                )
            }
        }
    ) {

    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}