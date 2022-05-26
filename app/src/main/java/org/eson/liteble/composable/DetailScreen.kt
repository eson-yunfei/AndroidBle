package org.eson.liteble.composable

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattService
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.eson.liteble.data.AppCommonData
import org.eson.liteble.data.SendCharacteristicsBean
import org.eson.liteble.ext.ActionExt
import org.eson.liteble.utils.BleUUIDUtil
import org.eson.liteble.viewmodels.ConnectViewModel
import java.util.*

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
                    Text(text = AppCommonData.selectDevice.value?.device?.name ?: "设备详情")
                },
                actions = {
                    Text(text = connectViewModel.connectedState.value)
                    IconButton(onClick = {
                        connectViewModel.showLogWindow.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.LibraryBooks, contentDescription = "log",
                            tint = Color.White
                        )
                    }
                })
        }
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(Modifier.weight(weight = 1f, fill = true)) {
                GattInfoLayout(gatt = connectViewModel.connectedGatt.value)
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
                    ActionExt.writeData(it.gatt, it.serviceUUID, it.characteristics, data)
                    AppCommonData.sendDataCharacteristic.value = null
                }
            }) {
                AppCommonData.sendDataCharacteristic.value = null
            }
        }

    }
}

@Composable
fun GattInfoLayout(gatt: BluetoothGatt?) {
    gatt?.let {
        LazyColumn(
            contentPadding = PaddingValues(8.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), //item 间距
        ) {
            items(gatt.services) { gattService: BluetoothGattService ->
                GattServiceItem(gatt = gatt, gattService = gattService)
            }
        }
    }
}

@Composable
fun GattServiceItem(gatt: BluetoothGatt, gattService: BluetoothGattService) {
    val uuid = gattService.uuid

    Card(modifier = Modifier.fillMaxWidth()) {


        Column(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            ServiceTopLayout(serviceUUid = uuid)
            gattService.characteristics?.let { list ->

                if (list.isNotEmpty()) {

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(color = Color(0xffefefef))
                            .padding(16.dp, 10.dp)
                    ) {

                        list.forEach {
                            ItemCharacteristics(gatt, gattService, it, {
                                val sendCharacteristicsBean =
                                    SendCharacteristicsBean(gatt, gattService.uuid, it.uuid)
                                AppCommonData.sendDataCharacteristic.value = sendCharacteristicsBean
                            }) {
                                MainScope().launch {
                                    ActionExt.enableNotify(
                                        gatt,
                                        gattService.uuid, it.uuid
                                    )
                                }
                            }
                        }
                    }
                }

            }


        }
    }

}


@Composable
fun ServiceTopLayout(serviceUUid: UUID) {
    val serviceName = BleUUIDUtil.getServiceNameByUUID(serviceUUid)
    val shortUUID = BleUUIDUtil.getHexValue(serviceUUid)

    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp, 10.dp)
    ) {
        Text(
            text = serviceName, style = TextStyle(
                color = Color(0xff666666),
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(
            modifier = Modifier
                .height(3.dp)
        )
        Row(Modifier.wrapContentHeight()) {
            Text(
                text = shortUUID, style = TextStyle(
                    color = Color(0xff666666),
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = "(${serviceUUid})", style = TextStyle(
                    color = Color(0xff999999),
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.Bottom)
            )

        }

    }


}
