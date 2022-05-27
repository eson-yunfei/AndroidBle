package org.eson.liteble.composable.detail

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.eson.liteble.data.AppCommonData
import org.eson.liteble.data.SendCharacteristicsBean
import org.eson.liteble.viewmodels.ConnectViewModel

@SuppressLint("MissingPermission")
@Composable
fun DetailScreen(connectViewModel: ConnectViewModel, backClick: () -> Unit) {

    Scaffold(
        topBar = {
            DetailTopBar(
                name = AppCommonData.selectDevice.value?.device?.name,
                connectSate = connectViewModel.connectResultState.value,
                backClick = {
                    connectViewModel.disconnect()
                    backClick.invoke()
                    AppCommonData.clearMessageList()
                },
                onStateClick = {
                    connectViewModel.changeConnectState()
                }, imageButtonClick = {
                    connectViewModel.showLogWindow.value = !connectViewModel.showLogWindow.value
                })
        }
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(Modifier.weight(weight = 1f, fill = true)) {
                GattInfoLayout(connectViewModel, gatt = connectViewModel.connectedGatt.value)
            }
            if (connectViewModel.showLogWindow.value) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(color = Color.Black)
                ) {
                    LogContainer()
                }
            }

        }

        if (AppCommonData.sendDataCharacteristic.value != null) {
            SendDataDialog(onSendClick = { data ->
                AppCommonData.sendDataCharacteristic.value?.let {
                    connectViewModel.writeData(it.gatt, it.serviceUUID, it.characteristics, data)
                    AppCommonData.sendDataCharacteristic.value = null
                }
            }) {
                AppCommonData.sendDataCharacteristic.value = null
            }
        }

    }
}

@Composable
fun GattInfoLayout(connectViewModel: ConnectViewModel, gatt: BluetoothGatt?) {
    gatt?.let { blueGatt ->
        LazyColumn(
            contentPadding = PaddingValues(8.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), //item 间距
        ) {
            items(gatt.services) { gattService: BluetoothGattService ->

                val serviceUUIDString = gattService.uuid.toString()
                val characteristicUUIDString = getCharacteristicUUIDString(gattService)
                ItemGattService(serviceUUIDString,
                    characteristicUUIDString,
                    getCharacteristicsProperties = {
                        return@ItemGattService getCharacteristicsProperties(gattService, it)
                    },
                    onReadData = {
                        return@ItemGattService connectViewModel.readInfo(
                            blueGatt,
                            serviceUUIDString,
                            it
                        )
                    }, onWriteClick = {

                        AppCommonData.sendDataCharacteristic.value = SendCharacteristicsBean(
                            blueGatt, serviceUUIDString, it
                        )
                    }, onNotify = {

                        MainScope().launch {
                            connectViewModel.enableNotify(
                                blueGatt,
                                serviceUUIDString, it
                            )
                        }
                    })
            }
        }
    }
}


fun getCharacteristicsProperties(gattService: BluetoothGattService, characteristics: String): Int {
    val predicate: (BluetoothGattCharacteristic) -> Boolean = {
        it.uuid.toString()
            .toLowerCase(Locale.current) == characteristics.toLowerCase(Locale.current)
    }
    val gattCharacteristic = gattService.characteristics.find(predicate)
    return gattCharacteristic!!.properties
}

fun getCharacteristicUUIDString(gattService: BluetoothGattService): MutableList<String> {
    return gattService.characteristics.map {
        it.uuid.toString()
    }.toMutableList()
}

