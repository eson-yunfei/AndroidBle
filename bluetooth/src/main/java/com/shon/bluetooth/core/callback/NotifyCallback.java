package com.shon.bluetooth.core.callback;

/**
 * Auth : xiao.yunfei
 * Date : 2020/10/05 21:02
 * Package name : com.shon.bluetooth.contorller.imp
 * Des :
 */
public abstract class NotifyCallback implements ICallback,OnTimeout {

    public abstract boolean getTargetSate();

    public String getDescriptor(){
        return "00002902-0000-1000-8000-00805F9B34FB";
    }
    @Override
    public boolean process(String address, byte[] result) {
        return false;
    }

    public void onChangeResult(boolean result){}
}
