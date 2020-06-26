package com.shon.dispatcher;

import com.shon.dispatcher.core.DispatcherConfig;
import com.shon.dispatcher.utils.TransLog;
import com.shon.dispatcher.utils.Utils;

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
        Class<?> commandApi = dispatcherConfig.getServerInterface();
        Utils.validateServiceInterface(dispatcherConfig.getServerInterface());

        return (T) Proxy.newProxyInstance(commandApi.getClassLoader(), new Class<?>[]{commandApi},
                dispatcherConfig.getInvocation());
    }


    /**
     *
     * @param receivedData receivedData
     */
    void receiverData(TMessage receivedData) {
        dispatcherConfig.getInvocation().addMessage(receivedData);
    }

    /**
     *
     * @param tMessage tMessage
     */
    void sendSuccess(TMessage tMessage) {

        dispatcherConfig.getInvocation().sendSuccess(tMessage);
    }
}
