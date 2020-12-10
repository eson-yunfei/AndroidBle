package org.eson.liteble.common.share

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

abstract class Preferences<T> constructor(context: Context,private val key: String, private val default: T) {

    abstract fun getShareName(): String

    private val preferences: SharedPreferences by lazy {
        context.applicationContext.getSharedPreferences(getShareName(), Context.MODE_PRIVATE)
    }

    open operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharedPreferences(key, default)
    }

    open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putSharedPreferences(key, value)
    }


    private fun <T> getSharedPreferences(name: String, default: T): T = with(preferences) {
        val value = when (default) {
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Long -> getLong(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalStateException("This is not saved")
        }
        return value as T
    }


    private fun <T> putSharedPreferences(name: String, value: T) = with(preferences.edit()) {
        when (value) {
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Long -> putLong(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalStateException("This is not saved")

        }
    }.apply()

}