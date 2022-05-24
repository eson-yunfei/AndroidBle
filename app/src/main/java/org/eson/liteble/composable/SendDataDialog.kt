package org.eson.liteble.composable

import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SendDataDialog(onSendClick: (sendData: String) -> Unit, onDismiss: () -> Unit) {
    val inputValue = remember {
        mutableStateOf("")
    }

    AlertDialog(onDismissRequest = { onDismiss.invoke() }, title = {
        Text(text = "输入要发送的数据(16进制字符串)")
    }, confirmButton = {
        TextButton(onClick = {
            onSendClick.invoke(inputValue.value)
            onDismiss.invoke()
        }) {
            Text(text = "发送")
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismiss.invoke()
        }) {
            Text(text = "取消")
        }
    }, text = {
        OutlinedTextField(value = inputValue.value, onValueChange = {
            inputValue.value = it
        }, placeholder = {
            Text(text = "eg: 0100ab")
        })
//            , keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.H)
    })
}

@Preview
@Composable
fun PreviewSendDialog() {
    SendDataDialog({}, {})
}