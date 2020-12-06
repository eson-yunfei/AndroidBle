package com.shon.bluetooth.core.callback;

/**
 * Auth : xiao.yunfei
 * Date : 2020/10/05 19:27
 * Package name : com.shon.bluetooth.contorller.imp
 * Des :
 */
public interface ICallback {
    boolean process(String address,byte[] result);
}
