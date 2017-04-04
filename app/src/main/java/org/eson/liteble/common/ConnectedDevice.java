package org.eson.liteble.common;

import android.os.Bundle;

import org.eson.liteble.bean.ServiceBean;

import java.util.HashMap;
import java.util.List;

/**
 * @package_name org.eson.liteble.common
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/4/3.
 * @description
 */

public class ConnectedDevice {
    private static ConnectedDevice connectedDevice = null;


    private HashMap<String, List<ServiceBean>> serviceMap = null;

    private ConnectedDevice() {
        serviceMap = new HashMap<>();
    }

    public static ConnectedDevice get() {
        if (connectedDevice == null) {
            connectedDevice = new ConnectedDevice();
        }
        return connectedDevice;
    }


    public void addConnectMap(String address, List<ServiceBean> serviceList) {

        if (serviceMap == null) {
            serviceMap = new HashMap<>();
        }

        if (!serviceMap.containsKey(address)) {
            serviceMap.put(address, serviceList);
            return;
        }
    }

    public List<ServiceBean> getServiceList(String address) {
        if (serviceMap == null) {
            return null;
        }
        if (!serviceMap.containsKey(address)) {
            return null;
        }
        return serviceMap.get(address);
    }

    public void removeConnectMap(String address) {
        if (serviceMap == null || serviceMap.size() == 0) {
            return;
        }
        if (!serviceMap.containsKey(address)) {
            return;
        }
        serviceMap.remove(address);
    }
}
