AndroidBle
------
一个蓝牙BLE 测试，调试工具以及开发SDK


[![](https://jitpack.io/v/eson-yunfei/MyTest.svg)](https://jitpack.io/#eson-yunfei/MyTest)

项目发布到 JitPack

## SDK 接入

### 1、初始化



```kotlin
BLEManager.init(this)
```



### 2、扫描设备



```kotlin
val scannerCompat = BluetoothLeScannerCompat.getScanner()
val scanCfg = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .setLegacy(false)
        .setReportDelay(1000)
        .setUseHardwareBatchingIfSupported(false)
        .build()
val filters: MutableList<ScanFilter> = mutableListOf()
val filter = ScanFilter.Builder()
        .setServiceUuid(ParcelUuid.fromString("your service uuid "))
        .setDeviceAddress("device address ")
        .build()

filters.add(filter)
scannerCompat.startScan(filters, scanCfg, scanCallback)
```





### 3、连接设备



```kotlin
Connect(address).enqueue(connectCallback)
```



### 4、断开设备



```kotlin
BLEManager.getInstance().disconnectDevice(deviceMac);
```

### 5、读取信息



```kotlin
ReadCall(connectMac)
        .setServiceUUid(serviceUUID)
        .setCharacteristicUUID(characterUUID)
        .enqueue(object : ReadCallback() {
            override fun process(address: String, result: ByteArray): Boolean {
                changeBleData(characterUUID, result, address)
                return true
            }

            override fun onTimeout() {}
        })
```



### 6、发送数据



```kotlin
WriteCall(connectMac)
        .setServiceUUid(serviceUUID)
        .setCharacteristicUUID(characterUUID)
        .enqueue(object : WriteCallback(connectMac) {
            override fun getSendData(): ByteArray {
                return buffer
            }

            override fun process(address: String, result: ByteArray): Boolean {
                return false
            }

            override fun removeOnWriteSuccess(): Boolean {
                return true
            }

            override fun onTimeout() {}
        })
```



### 7、开启监听

```kotlin
NotifyCall(connectMac)
        .setServiceUUid(serviceUUID)
        .setCharacteristicUUID(characterUUID)
        .enqueue(object : NotifyCallback() {
            override fun getTargetSate(): Boolean {
                return true
            }

            override fun onChangeResult(result: Boolean) {
                super.onChangeResult(result)
                startListener()
            }

            override fun onTimeout() {}
        })
        
        
        
     fun startListener() {
        Listener(connectMac)
                .enqueue { address: String?, result: ByteArray? ->
                    changeBleData("", result, address)
                    true
                }
    }
```

####  License


        Copyright (c) 2017 xiaoyunfei
    
        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at
    
                http://www.apache.org/licenses/LICENSE-2.0
    
        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.


