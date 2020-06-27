package com.e.tool.ble.annotation;

import android.bluetooth.BluetoothProfile;

import androidx.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 19:14
 * Package name : com.e.tool.ble.annotation
 * Des :
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,
        ElementType.METHOD,
        ElementType.FIELD})
@IntDef(
        value = {
                BluetoothProfile.STATE_CONNECTING,
                BluetoothProfile.STATE_CONNECTED,
                BluetoothProfile.STATE_DISCONNECTED,
                BluetoothProfile.STATE_DISCONNECTING
        }
)
public @interface LinkState {
}
