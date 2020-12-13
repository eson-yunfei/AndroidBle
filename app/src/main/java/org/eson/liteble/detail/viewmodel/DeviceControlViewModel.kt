package org.eson.liteble.detail.viewmodel

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic.*
import android.bluetooth.BluetoothGattService
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.eson.liteble.detail.bean.CharacterBean
import org.eson.liteble.detail.bean.DescriptorBean
import org.eson.liteble.detail.bean.ServiceBean


class DeviceControlViewModel @ViewModelInject
constructor(@Assisted private val savedStateHandle: SavedStateHandle) : ViewModel() {

    val mutableLiveData = MutableLiveData<List<ServiceBean>>()

     fun loadGattServer(gatt: BluetoothGatt) {

        val services = gatt.services
        if (services == null || services.isEmpty()) {
            return
        }
        val serviceBeanList: List<ServiceBean> = services.map { bleGattService ->
            val serviceBean = ServiceBean()
            val serviceUUID = bleGattService.uuid.toString()
            serviceBean.serviceUUID = serviceUUID
            serviceBean.serviceType = getServerType(bleGattService.type)

            val characteristic = bleGattService.characteristics

            characteristic?.let { characteristicList ->

                val characterBeanList = characteristicList.map { bleGattCharacteristic ->

                    val characterBean = CharacterBean(gatt.device.address,gatt.device.name
                    ,bleGattCharacteristic.uuid.toString(),serviceUUID)

                    ////用于区分特性用途（读、写、通知）
                    val properties = bleGattCharacteristic.properties
                    if ((properties and PROPERTY_READ) != 0) {
                        characterBean.read = true
                    }
                    if ((properties and PROPERTY_WRITE) != 0) {
                        characterBean.write = true
                    }
                    if ((properties and PROPERTY_NOTIFY) != 0) {
                        characterBean.notify = true
                    }
                    val descriptors = bleGattCharacteristic.descriptors
                    descriptors?.let { descriptorList ->
                        val descriptorBeanList = descriptorList.map { descriptor ->
                            val descriptorBean = DescriptorBean()
                            descriptorBean.uuid = descriptor.uuid.toString()
                            descriptorBean.permissions = descriptor.permissions
                            descriptorBean
                        }
                        characterBean.descriptorBeen = descriptorBeanList
                    }
                    characterBean
                }
                serviceBean.uuidBeen = characterBeanList
            }
            serviceBean
        }
        mutableLiveData.postValue(serviceBeanList)
    }

    private fun getServerType(serviceType: Int): String {
        return if (serviceType == BluetoothGattService.SERVICE_TYPE_PRIMARY) {
            "PRIMARY"
        } else {
            "SECONDARY"
        }
    }

}