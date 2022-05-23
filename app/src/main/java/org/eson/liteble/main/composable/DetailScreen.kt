package org.eson.liteble.main.composable

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.eson.liteble.R
import org.eson.liteble.common.util.BleUUIDUtil
import org.eson.liteble.main.ConnectViewModel
import org.eson.liteble.main.selectDevice
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
                    Text(text = selectDevice.value?.device?.name ?: "设备详情")
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
            if (connectViewModel.showLogWindow.value){
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(color = Color.Black)
                ) {

                }
            }

        }

        if (connectViewModel.showSendDataDialog.value){
            SendDataDialog(onSendClick = {
                connectViewModel.showSendDataDialog.value = false
            }) {
                connectViewModel.showSendDataDialog.value = false
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
                            CharacteristicsItem(
                                gatt = gatt,
                                gattService = gattService,
                                characteristics = it
                            )
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

@Composable
fun CharacteristicsItem(
    gatt: BluetoothGatt,
    gattService: BluetoothGattService,
    characteristics: BluetoothGattCharacteristic,
) {
    val connectViewModel: ConnectViewModel = viewModel()
    val characterUUID: UUID = characteristics.uuid!!
    val characterName = BleUUIDUtil.getCharacterNameByUUID(characterUUID)
    val hexString = BleUUIDUtil.getHexValue(characterUUID)
    val readResult = remember {
        mutableStateOf("")
    }
    Row(Modifier.fillMaxWidth()) {
        Column {
            Text(
                text = characterName,
                style = TextStyle(
                    color = Color(0xff666666),
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Serif,
                )
            )

            Row {
                Text(
                    text = hexString,
                    style = TextStyle(
                        color = Color(0xff666666),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Serif,
                    )
                )
                Text(
                    text = "($characterUUID)",
                    style = TextStyle(
                        color = Color(0xff666666),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Serif,
                    ),
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }

            Text(
                text = readResult.value,
                style = TextStyle(
                    color = Color(0xff666666),
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                )
            )
        }


        Row(
            Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.End
        ) {
            val properties = characteristics.properties
            if ((properties and BluetoothGattCharacteristic.PROPERTY_READ) != 0) {
                IconButton(onClick = {
                    MainScope().launch {
                        val readInfo = connectViewModel.readInfo(
                            gatt,
                            gattService.uuid.toString(),
                            characteristics.uuid.toString()
                        )
                        readInfo?.let {
                            readResult.value = it
                        }

                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_operation_read_normal),
                        contentDescription = "read"
                    )
                }
            }
            if ((properties and BluetoothGattCharacteristic.PROPERTY_WRITE) != 0) {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_operation_send_normal),
                        contentDescription = "write"
                    )
                }
            }
            if ((properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_operation_start_notifications_normal),
                        contentDescription = "NOTIFY"
                    )
                }
            }
        }
    }

}