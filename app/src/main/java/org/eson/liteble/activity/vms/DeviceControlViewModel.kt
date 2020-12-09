package org.eson.liteble.activity.vms

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic.*
import android.bluetooth.BluetoothGattService
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.shon.bluetooth.util.BleUUIDUtil
import org.eson.liteble.DeviceState.Companion.instance
import org.eson.liteble.DeviceState.DeviceLiveData
import org.eson.liteble.activity.bean.CharacterBean
import org.eson.liteble.activity.bean.DescriptorBean
import org.eson.liteble.activity.bean.ServiceBean

class DeviceControlViewModel : ViewModel() {
    private var connectLiveData: DeviceLiveData? = null
    val mutableLiveData = MutableLiveData<List<ServiceBean>>()
    fun setConnectDevice(connectAddress: String?) {
        val instance = instance ?: return
        connectLiveData = instance.getDeviceLiveData(connectAddress!!)
        if (connectLiveData == null) {
            return
        }
        val gatt = connectLiveData!!.gatt ?: return
        loadGattServer(gatt)
    }

    fun observerDeviceLiveData(lifecycleOwner: LifecycleOwner?,observer: Observer<DeviceLiveData?>) {
        if (connectLiveData == null) {
            return
        }
        connectLiveData!!.observe(lifecycleOwner!!, observer)
    }






    private fun loadGattServer(gatt: BluetoothGatt) {

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

            characteristic?.let {

                val characterBeanList = it.map { bleGattCharacteristic ->

                    val characterBean = CharacterBean()
                    characterBean.characterUUID = bleGattCharacteristic.uuid.toString()
                    characterBean.serviceUUID = serviceUUID
                    ////用于区分特性用途（读、写、通知）
                    val properties = bleGattCharacteristic.properties
                    if ((properties and PROPERTY_READ) != 0) {
                        characterBean.isRead = true
                    }
                    if ((properties and PROPERTY_WRITE) != 0) {
                        characterBean.isWrite = true
                    }
                    if ((properties and PROPERTY_NOTIFY) != 0) {
                        characterBean.isNotify = true
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