package org.eson.liteble.composable.detail

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shon.ble.data.ConnectResult
import com.shon.ble.util.BleLog

@Composable
fun DetailTopBar(
    name: String?,
    connectSate: ConnectResult,
    backClick: () -> Unit,
    onStateClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                backClick.invoke()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
            }
        },
        title = {
            Text(text = name ?: "UnKnow")
        },
        actions = {

            if (connectSate is ConnectResult.Connecting) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp),
                    strokeWidth = 2.dp
                )
            }

            TextButton(onClick = {
                onStateClick.invoke()
            }, enabled = connectSate !is ConnectResult.Connecting) {
                Text(
                    text = getScanStateString(connectSate),
                    style = TextStyle(
                        color = Color.White
                    )
                )
            }

        }
    )
}


private fun getScanStateString(connectResult: ConnectResult): String {
    BleLog.d("getScanStateString connectResult = $connectResult")
    return when (connectResult) {
        is ConnectResult.Connecting -> "Connecting"
        is ConnectResult.ConnectSuccess -> "DisConnect"
        else -> "Start Connect"
    }
}

@Preview
@Composable
fun PreviewDetailTop() {
    DetailTopBar(
        name = "BLE_E1",
        connectSate = ConnectResult.Connecting(""),
        backClick = {},
        onStateClick = {})
}