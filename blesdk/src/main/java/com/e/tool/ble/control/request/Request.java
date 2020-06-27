package com.e.tool.ble.control.request;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/27 12:55
 * Package name : com.e.tool.ble.control
 * Des :
 */
public abstract class Request {

    public boolean isWaiting = true;

    public abstract boolean launch();
}
