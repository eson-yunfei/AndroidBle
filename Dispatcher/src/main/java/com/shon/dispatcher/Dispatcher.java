package com.shon.dispatcher;

import com.shon.dispatcher.bean.Message;
import com.shon.dispatcher.utils.TransLog;

import java.lang.reflect.Proxy;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:06
 * Package name : com.shon.dispatcher
 * Des :
 */
public class Dispatcher {

    private static Dispatcher dispatcher = null;

    private DispatcherConfig dispatcherConfig;



    private Invocation invocation;
    private Dispatcher() {
    }

    public static void init(DispatcherConfig dispatcherConfig) {
        getInstance().setConfig(dispatcherConfig);
    }

    private void setConfig(DispatcherConfig dispatcherConfig) {
        this.dispatcherConfig = dispatcherConfig;

        TransLog.e("dispatcherConfig : " + dispatcherConfig.toString());
    }

    public static Dispatcher getInstance() {
        if (dispatcher == null) {
            synchronized (Dispatcher.class) {
                if (dispatcher == null) {
                    dispatcher = new Dispatcher();
                }
            }
        }
        return dispatcher;
    }


    public <T> T getApi() {

        if (dispatcherConfig == null || dispatcherConfig.getServerInterface() == null) {
            return null;
        }
        if (invocation == null){
            invocation = new Invocation(dispatcherConfig.getTransmitter());
        }
        Class<?> commandApi = dispatcherConfig.getServerInterface();
        Utils.validateServiceInterface(dispatcherConfig.getServerInterface());

        return (T) Proxy.newProxyInstance(commandApi.getClassLoader(), new Class<?>[]{commandApi},
                invocation);
    }


    public void receiverData(Message receivedData) {
        TransLog.e("Dispatcher -->>  receiverData  : " + receivedData.toString());

        if (invocation == null){
            return;
        }
        invocation.addMessage(receivedData);
    }

}
