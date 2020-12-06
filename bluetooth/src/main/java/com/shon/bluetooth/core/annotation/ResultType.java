package com.shon.bluetooth.core.annotation;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.SOURCE)
@IntDef({
        BluetoothGattCharacteristic.PROPERTY_READ,
        BluetoothGattCharacteristic.PROPERTY_WRITE,
        BluetoothGattCharacteristic.PROPERTY_NOTIFY,
})
public @interface ResultType {
}
