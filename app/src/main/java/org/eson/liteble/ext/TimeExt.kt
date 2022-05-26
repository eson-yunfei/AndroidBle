package org.eson.liteble.ext

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ConstantLocale")
private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault())


fun getCurrentTime(): String {
    return simpleDateFormat.format(Date(System.currentTimeMillis()))
}