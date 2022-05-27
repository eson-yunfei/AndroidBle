package org.eson.liteble.composable.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.eson.liteble.BuildConfig
import org.eson.liteble.R
import org.eson.liteble.data.AppCommonData

@Composable
fun AppInfoDialog() {

    AlertDialog(onDismissRequest = {
        AppCommonData.showAboutState.value = false
    }, buttons = {

    }, text = {
        AppInfoDialogContent()
    })

}


@Composable
fun AppInfoDialogContent(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp, 16.dp)
    ) {
        Text(
            text = "About Lite Ble",
            style = TextStyle(
                color = Color(0xff555555),
                fontSize = 18.sp
            ), modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.requiredHeight(50.dp))

        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = "App Icon",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.requiredHeight(50.dp))
        Text(
            text = "App Version : V${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})",
            style = TextStyle(
                color = Color(0xff555555),
                fontSize = 14.sp
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        Text(
            text = "BLE SDK Version : V${com.shon.bluetooth.BuildConfig.VERSION_NAME}(${com.shon.bluetooth.BuildConfig.VERSION_CODE})",
            style = TextStyle(
                color = Color(0xff555555),
                fontSize = 14.sp
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

}
@Preview
@Composable
fun PreviewAppInfo() {
    MaterialTheme {
        AppInfoDialog()
    }
}