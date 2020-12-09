package com.shon.bluetooth.core.annotation;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.SOURCE)
@IntDef({
        Constants.PROPERTY_READ,
        Constants.PROPERTY_WRITE,
        Constants.PROPERTY_NOTIFY,
        Constants.PROPERTY_READ_RSSI
})
public @interface ResultType {
}
