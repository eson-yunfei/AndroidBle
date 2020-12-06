//package org.eson.liteble.activity.vms;
//
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothGattService;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.e.ble.util.BleUUIDUtil;
//
//import org.eson.liteble.activity.bean.CharacterBean;
//import org.eson.liteble.activity.bean.DescriptorBean;
//import org.eson.liteble.activity.bean.ServiceBean;
//import org.eson.liteble.util.LogUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
//import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
//import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE;
//
///**
// * Auth : xiao.yunfei
// * Date : 2020/6/20 17:51
// * Package name : org.eson.liteble.activity.vms
// * Des :
// */
//public class ServiceListViewModel extends ViewModel {
//
//    private MutableLiveData<List<ServiceBean>> mutableLiveData;
//    public MutableLiveData<List<ServiceBean>>  getServiceList(BluetoothGatt bluetoothGatt) {
//        mutableLiveData = new MutableLiveData<>();
//
//        List<BluetoothGattService> serviceArrayList = bluetoothGatt.getServices();
//
//        if (serviceArrayList == null || serviceArrayList.size() == 0) {
//            return mutableLiveData;
//        }
//
//        List<ServiceBean> serviceBeanList = new ArrayList<>();
//
//        ServiceBean serviceBean;
//        for (BluetoothGattService service : serviceArrayList) {
//            serviceBean = new ServiceBean();
//            UUID serviceUUID = service.getUuid();
//            String typeStr = getServerType(serviceBean, service, serviceUUID);
//
//            serviceBean.setServiceUUID(serviceUUID.toString());
//            serviceBean.setServiceType(typeStr);
//
//            List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
//
//            if (gattCharacteristics == null || gattCharacteristics.size() == 0) {
//                serviceBeanList.add(serviceBean);
//                continue;
//            }
//
//            List<CharacterBean> characterBeanList = getCharacters(serviceUUID,gattCharacteristics);
//            serviceBean.setUUIDBeen(characterBeanList);
//            serviceBeanList.add(serviceBean);
//        }
//
//        mutableLiveData.postValue(serviceBeanList);
//        return mutableLiveData;
//    }
//
//    private String getServerType(ServiceBean serviceBean, BluetoothGattService service, UUID serviceUUID) {
//
//        int serviceType = service.getType();
//        return (serviceType == BluetoothGattService.SERVICE_TYPE_PRIMARY)
//                ? "PRIMARY" : "SECONDARY";
//    }
//
//    private List<CharacterBean> getCharacters(@NonNull UUID serviceUUID,
//                                              @NonNull List<BluetoothGattCharacteristic> gattCharacteristics){
//
//        List<CharacterBean> characterBeanList = new ArrayList<>();
//
//        CharacterBean characterBean;
//        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
//            characterBean = new CharacterBean();
//
//            UUID character = gattCharacteristic.getUuid();
//            String characterString = character.toString();
//            characterBean.setCharacterUUID(characterString);
//            characterBean.setServiceUUID(serviceUUID.toString());
//
//            setEnableFunction(characterBean, gattCharacteristic, character);
//
//            List<BluetoothGattDescriptor> descriptorList = gattCharacteristic.getDescriptors();
//
//            if (descriptorList == null || descriptorList.size() == 0) {
//                characterBeanList.add(characterBean);
//                continue;
//            }
//            List<DescriptorBean> descriptorBeen = getDescriptors(descriptorList);
//            characterBean.setDescriptorBeen(descriptorBeen);
//            characterBeanList.add(characterBean);
//        }
//        return characterBeanList;
//    }
//
//    private void setEnableFunction(CharacterBean characterBean, BluetoothGattCharacteristic gattCharacteristic,
//                                   UUID character) {
//        int characterValue = BleUUIDUtil.getValue(character);
//        LogUtil.e("characterValue:" + Integer.toHexString(characterValue));
//
//        int properties = gattCharacteristic.getProperties();    //用于区分特性用途（读、写、通知）
//
//        if ((properties & PROPERTY_READ) != 0) {
//            characterBean.setRead(true);
//        }
//        if ((properties & PROPERTY_WRITE) != 0) {
//            characterBean.setWrite(true);
//        }
//        if ((properties & PROPERTY_NOTIFY) != 0) {
//            characterBean.setNotify(true);
//        }
//    }
//
//    private List<DescriptorBean> getDescriptors(@NonNull List<BluetoothGattDescriptor> gattDescriptors){
//        List<DescriptorBean> descriptorBeen = new ArrayList<>();
//        DescriptorBean descriptorBean;
//        for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
//            UUID des = gattDescriptor.getUuid();
//            int permissions = gattDescriptor.getPermissions();
//            descriptorBean = new DescriptorBean();
//            descriptorBean.setUUID(des.toString());
//            descriptorBean.setPermissions(permissions);
//            descriptorBeen.add(descriptorBean);
//        }
//        return descriptorBeen;
//    }
//
//}
