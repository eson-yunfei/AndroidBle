package org.eson.liteble.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.eson.liteble.data.AppCommonData
import org.eson.liteble.ext.getCurrentTime
import org.eson.liteble.logger.LogMessageBean

@Composable
fun LogContainer() {
    Box(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
    ) {
        val listState = rememberLazyListState()
        LaunchedEffect(AppCommonData.messageList.size) {
            listState.animateScrollToItem(AppCommonData.messageList.size)
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp), //item 间距
            state = listState
        ) {
            items(AppCommonData.messageList) { item: LogMessageBean ->
                ItemLog(createTime = item.createTime, title = item.title, content = item.content)
            }
        }
    }
}

@Composable
fun ItemLog(createTime: String, title: String, content: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = createTime,
            style = TextStyle(
                color = Color.White
            )
        )
        Text(
            text = title, style = TextStyle(
                color = Color.White
            )
        )
        Text(
            text = content, style = TextStyle(
                color = Color.White
            )
        )
    }
}

@Preview
@Composable
fun PreviewLogContainer() {
    val uuid = "00002a27-0000-1000-8000-00805f9b34fb"
    val address = "11:22:33:44:55:66"
    val content = "UUID: $uuid"
    val logMessageBean = LogMessageBean(address, "Read Info ($address)", content)
    AppCommonData.addMessageList(logMessageBean)

    val result = ""
    val logMessageBean2 = LogMessageBean(address, "Read Result ($address)", result)
    AppCommonData.addMessageList(logMessageBean2)

    LogContainer()
}

@Preview
@Composable
fun PreviewItemLog() {
    ItemLog(
        getCurrentTime(),
        "Read from -->> 11:22:33:44:55:66",
        "UUID: 00002902-0000-1000-8000-00805F9B34FB"
    )
}