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


 五、蓝牙连接



        BLEControl.get().enableNotify(serviceUuid, characteristicUuid,
        descriptorUui, new BLEConnectCallBack() {
                            @Override
                        	public void onConnecting() {

                        		sendBleState(BLEConstant.State.STATE_CONNECTING);
                        	}

                        	@Override
                        	public void onConnected() {
                        		sendBleState(BLEConstant.State.STATE_CONNECTED);
                        	}

                        	@Override
                        	public void onDisConnecting() {
                        		sendBleState(BLEConstant.State.STATE_DIS_CONNECTING);
                        	}

                        	@Override
                        	public void onDisConnected() {
                        		sendBleState(BLEConstant.State.STATE_DIS_CONNECTED);
                        	}

                        	@Override
                        	public void onBleServerEnable() {
                        		sendBleState(BLEConstant.State.STATE_DISCOVER_SERVER);

                        	}
                        });