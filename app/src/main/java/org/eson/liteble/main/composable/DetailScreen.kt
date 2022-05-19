package org.eson.liteble.main.composable

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.eson.liteble.main.ConnectViewModel
import org.eson.liteble.main.selectDevice

@SuppressLint("MissingPermission")
@Composable
fun DetailScreen(backClick: () -> Unit) {
    val connectViewModel: ConnectViewModel = viewModel()

    connectViewModel.startConnectDevice()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        backClick.invoke()
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                title = {
                    Text(text = selectDevice.value?.device?.name ?: "设备详情")
                },
                actions = {
                    Text(text = connectViewModel.connectedState.value)
                })
        }
    ) {

        GattInfoLayout(gatt = connectViewModel.connectedGatt.value)


    }
}

@Composable
fun GattInfoLayout(gatt: BluetoothGatt?) {
    gatt?.let {
        LazyColumn {
            items(gatt.services) { gattService: BluetoothGattService ->
                GattServiceItem(gatt = gatt, gattService = gattService)
            }
        }
    }
}

@Composable
fun GattServiceItem(gatt: BluetoothGatt, gattService: BluetoothGattService) {

    Column (Modifier.wrapContentSize()){
        Text(
            text = gattService.uuid.toString(), style = TextStyle(
                color = Color(0xff666666),
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
        )
//        LazyColumn {
//            items(gattService.characteristics) { characteristics ->
//
//                CharacteristicsItem(gatt,gattService,characteristics)
//            }
//        }
    }
}

@Composable
fun CharacteristicsItem(
    gatt: BluetoothGatt,
    gattService: BluetoothGattService,
    characteristics: BluetoothGattCharacteristic
) {
    Row() {
        Column {
            Text(
                text = characteristics.uuid.toString(),
                style = TextStyle(
                    color = Color(0xff666666),
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }

}