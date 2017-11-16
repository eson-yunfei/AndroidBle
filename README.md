AndroidBle
------
一个蓝牙BLE 测试，调试工具以及开发SDK


[![](https://jitpack.io/v/eson-yunfei/MyTest.svg)](https://jitpack.io/#eson-yunfei/MyTest)

项目发布到 JitPack


SDK 接入
------
####1.1、gradle 构建
       //在根目录： build.gradle 文件中添加：
        allprojects {
        		repositories {
        			...
       			maven { url 'https://jitpack.io' }
        		}
        	}
        	
       //在app的 build.gradle 文件中添加：
        
        dependencies {
     	        compile 'com.github.eson-yunfei:AndroidBle:xxxx'
     	  }

####1.2、下载源码，导入

API 指南
------
###1、SDK 初始化
        
    //之前版本
    // BLESdk.init();
    //3.19 修改之后
    BLESdk.get().init(mContext);

    设置最大连接的设备
    //BLESdk.get().setMaxConnect(3);


###2、蓝牙权限、状态等检测


        BLECheck.get().checkBleState(context, new BLECheckListener() {
			@Override
			public void noBluetoothPermission() {
                //未获取蓝牙权限,申请权限
                BLECheck.get().requestBlePermission();
                //申请成功之后，在进行检测
			}

			@Override
			public void notSupportBle() {
                //不支持蓝牙
			}

			@Override
			public void bleClosing() {
                //蓝牙未开启,开启蓝牙
                BLECheck.get().openBle();
                //监听蓝牙打开结果，之后检测
			}

			@Override
			public void bleStateOK() {
                //蓝牙状态一切正常，可以扫描、连接设备
			}
		});
		
###3、蓝牙搜索
####3.1 开启蓝牙扫描
        //扫描规则构造
        BLEScanCfg scanCfg = new BLEScanCfg.ScanCfgBuilder(timeOut)
                .addUUIDFilter(uuid1,uuid2)//可选，多个
                .addNameFilter(name1,name2)//可选，多个
                .builder();
                
        BLEScanner.get().startScanner(scanCfg, new BLEScanListener() {
            @Override
            public void onScannerStart() {
                super.onScannerStart();
                //可选
            }

            @Override
            public void onScanning(BLEDevice device) {
                //  必须，可直接刷新界面
            }


            @Override
            public void onScannerStop() {
               //可选
            }

            @Override
            public void onScannerError(int errorCode) {
               //可选
            }

            @Override
            public boolean isRemove() {
                //可选,默认返回true，扫描结束移除此listener
                return super.isRemove();
            }
        });
        
####3.2 停止蓝牙搜索
        //停止扫描
        BLEScanner.get().stopScan();



### 四、蓝牙交互

#### 1、添加事件监听
        BLEControl.get().setBleConnectListener(bleConnectionListener);
        BLEControl.get().setBleStateChangedListener(stateChangeListener);
        BLEControl.get().setBleTransportListener(transportListener);

#### 2、设备连接

             BLEControl.get().connectDevice(context, bleMac);


#### 3、启用Notify特性

        BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(serviceUuid, characteristicUuid)
			        .setAddress(address)
			        .setDescriptorUUID(descriptorUui)
			        .setEnable(isListenerNotice).builder();
			        
        BLEControl.get().enableNotify(bleUuid);


#### 4、发送数据
        BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(serviceUuid, characteristicUuid)
                .setAddress(address).setDataBuffer(bytes).builder();
                
        BLEControl.get().sendData(bleUuid);



#License


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


