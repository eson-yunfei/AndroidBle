# AndroidBle
蓝牙

##2017/02/22
一、创建仓库，首次提交项目代码


二、初始化

      BLESdk.init();

三、蓝牙权限、状态等检测


        BLECheck.get().checkBleState(context, new BLECheckListener() {
			@Override
			public void noBluetoothPermission() {
                //未获取蓝牙权限,申请权限
                BLECheck.get().requestBlePermission();
			}

			@Override
			public void notSupportBle() {
                //不支持蓝牙
			}

			@Override
			public void bleClosing() {
                //蓝牙未开启,开启蓝牙
                BLECheck.get().openBle();
			}

			@Override
			public void bleStateOK() {
                //蓝牙状态一切正常，可以扫描、连接设备
			}
		});

四、蓝牙搜索

            BLEScanner.get().startScan(0, nameFilter, uuidFilter, new BLEScanListener() {
    			@Override
    			public void onScannerStart() {
    				//开始扫描
    			}

    			@Override
    			public void onScanning(BLEDevice device) {
                    //扫描设备中
    			}

    			@Override
    			public void onScannerStop() {
                    //扫描停止
    			}

    			@Override
    			public void onScannerError() {
                    //扫描出错
    			}
    		});


 五、蓝牙交互



      1、添加时间监听



        BLEControl.get().setBleConnectListener(bleConnectionListener);
		BLEControl.get().setBleStateChangedListener(stateChangeListener);
		BLEControl.get().setBleTransportListener(transportListener);





      2、设备连接


        BLEControl.get().connectDevice(context, bleMac);


      3、启用Notify特性

        BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(serviceUuid, characteristicUuid)
        				.setAddress(address)
        				.setDescriptorUUID(descriptorUui)
        				.setEnable(isListenerNotice).builder();

        BLEControl.get().enableNotify(bleUuid);


      4、发送数据


        BLEUuid bleUuid = new BLEUuid.BLEUuidBuilder(serviceUuid, characteristicUuid)
      				.setAddress(address).setDataBuffer(bytes).builder();

        BLEControl.get().sendData(bleUuid);