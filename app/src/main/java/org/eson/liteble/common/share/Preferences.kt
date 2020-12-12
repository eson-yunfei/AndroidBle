package org.eson.liteble.common.share

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

/**
 * 对 String 做了一个扩展函数
 */
//internal fun <T> String.preferences(context: Context, default: T): T {
//    val result: T by Preferences(context, this, default)
//    return result
//}


@Suppress("UNCHECKED_CAST")
open class Preferences<T> constructor(context: Context, private val key: String, private val default: T) {

    open fun getShareName(): String? = null

    private val preferences: SharedPreferences by lazy {
        context.applicationContext.getSharedPreferences(getShareName() ?: key, Context.MODE_PRIVATE)
    }

    /**
     * 委托
     */
    open operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharedPreferences(key, default)
    }

    /**
     * 委托
     */
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