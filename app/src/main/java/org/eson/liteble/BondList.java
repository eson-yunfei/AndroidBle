package org.eson.liteble;

import androidx.lifecycle.MutableLiveData;

import com.e.tool.ble.bean.state.DevState;

import org.eson.liteble.activity.bean.BondedDeviceBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 15:49
 * Package name : org.eson.liteble
 * Des : 已连接设备列表
 */
public class BondList {
    private HashMap<String, BondedDeviceBean> mDeviceBeanMap;

    private MutableLiveData<List<BondedDeviceBean>> mutableLiveData;

    BondList() {
        mDeviceBeanMap = new HashMap<>();
        mutableLiveData = new MutableLiveData<>();
    }

    /**
     * 添加已连接设备
     *
     * @param devState DevState
     */
    void addBindDevice(DevState devState) {
        String address = devState.getAddress();
        if (isContainsDevice(address)) {
            return;
        }
        BondedDeviceBean deviceBean = new BondedDeviceBean(address);
        deviceBean.setName(devState.getName());
        deviceBean.setConnected(true);
        mDeviceBeanMap.put(address, deviceBean);
        updateBondList();
    }

    /**
     * 移除已连接设备
     *
     * @param devState DevState
     */
    void removeBondDevice(DevState devState) {
        String address = devState.getAddress();
        if (!isContainsDevice(address)) {
            return;
        }
        mDeviceBeanMap.remove(address);
        updateBondList();
    }

    /**
     * 刷新列表
     */
    private void updateBondList() {
        if (mutableLiveData == null) {
            return;
        }
        List<BondedDeviceBean> bondedDeviceBeans = new ArrayList<>();
        if (mDeviceBeanMap == null || mDeviceBeanMap.size() == 0) {
            mutableLiveData.postValue(bondedDeviceBeans);
            return;
        }
        for (Map.Entry<String, BondedDeviceBean> entry : mDeviceBeanMap.entrySet()) {
            BondedDeviceBean bondedDeviceBean = entry.getValue();
            bondedDeviceBeans.add(bondedDeviceBean);
        }
        mutableLiveData.postValue(bondedDeviceBeans);

    }


    /**
     * 获取 列表数据
     * @return MutableLiveData
     */
    public MutableLiveData<List<BondedDeviceBean>> observerBondList() {
        return mutableLiveData;
    }


    private boolean isContainsDevice(String address) {
        if (mDeviceBeanMap == null || mDeviceBeanMap.size() == 0) {
            return false;
        }
        return mDeviceBeanMap.containsKey(address);
    }

}
