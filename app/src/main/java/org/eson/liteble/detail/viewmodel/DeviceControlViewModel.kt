//package org.eson.liteble.detail.viewmodel
//
//import android.bluetooth.BluetoothGatt
//import android.bluetooth.BluetoothGattCharacteristic.*
//import android.bluetooth.BluetoothGattService
//import androidx.lifecycle.*
//import org.eson.liteble.common.DeviceState.Companion.instance
//import org.eson.liteble.common.DeviceState.DeviceLiveData
//import org.eson.liteble.detail.bean.CharacterBean
//import org.eson.liteble.detail.bean.DescriptorBean
//import org.eson.liteble.detail.bean.ServiceBean
//
//
//class DeviceControlViewModel
//constructor( private val savedStateHandle: SavedStateHandle) : ViewModel() {
//
//    private var connectLiveData: DeviceLiveData? = null
//    val mutableLiveData = MutableLiveData<List<ServiceBean>>()
//    fun setConnectDevice(connectAddress: String?) {
//        val instance = instance ?: return
//        connectLiveData = instance.getDeviceLiveData(connectAddress!!)
//        if (connectLiveData == null) {
//            return
//        }
//        val gatt = connectLiveData!!.gatt ?: return
//        loadGattServer(gatt)
//    }
//
//    fun observerDeviceLiveData(lifecycleOwner: LifecycleOwner?, observer: Observer<DeviceLiveData?>) {
//        if (connectLiveData == null) {
//            return
//        }
//        connectLiveData!!.observe(lifecycleOwner!!, observer)
//    }
//
//
//    private fun loadGattServer(gatt: BluetoothGatt) {
//
//        val services = gatt.services
//        if (services == null || services.isEmpty()) {
//            return
//        }
//        val serviceBeanList: List<ServiceBean> = services.map { bleGattService ->
//            val serviceBean = ServiceBean()
//            val serviceUUID = bleGattService.uuid.toString()
//            serviceBean.serviceUUID = serviceUUID
//            serviceBean.serviceType = getServerType(bleGattService.type)
//
//            val characteristic = bleGattService.characteristics
//
//            characteristic?.let { characteristicList ->
//
//                val characterBeanList = characteristicList.map { bleGattCharacteristic ->
//
//                    val characterBean = CharacterBean(gatt.device.address,gatt.device.name
//                    ,bleGattCharacteristic.uuid.toString(),serviceUUID)
//
//                    ////用于区分特性用途（读、写、通知）
//                    val properties = bleGattCharacteristic.properties
//                    if ((properties and PROPERTY_READ) != 0) {
//                        characterBean.read = true
//                    }
//                    if ((properties and PROPERTY_WRITE) != 0) {
//                        characterBean.write = true
//                    }
//                    if ((properties and PROPERTY_NOTIFY) != 0) {
//                        characterBean.notify = true
//                    }
//                    val descriptors = bleGattCharacteristic.descriptors
//                    descriptors?.let { descriptorList ->
//                        val descriptorBeanList = descriptorList.map { descriptor ->
//                            val descriptorBean = DescriptorBean()
//                            descriptorBean.uuid = descriptor.uuid.toString()
//                            descriptorBean.permissions = descriptor.permissions
//                            descriptorBean
//                        }
//                        characterBean.descriptorBeen = descriptorBeanList
//                    }
//                    characterBean
//                }
//                serviceBean.uuidBeen = characterBeanList
//            }
//            serviceBean
//        }
//        mutableLiveData.postValue(serviceBeanList)
//    }
//
//    private fun getServerType(serviceType: Int): String {
//        return if (serviceType == BluetoothGattService.SERVICE_TYPE_PRIMARY) {
//            "PRIMARY"
//        } else {
//            "SECONDARY"
//        }
//    }
//
//}