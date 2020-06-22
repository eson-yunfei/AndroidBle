package com.shon.dispatcher;

import com.shon.dispatcher.annotation.API;
import com.shon.dispatcher.annotation.Notice;
import com.shon.dispatcher.bean.Sender;
import com.shon.dispatcher.bean.Message;
import com.shon.dispatcher.utils.TransLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:06
 * Package name : com.shon.dispatcher
 * Des :
 */
public class Dispatcher {

    private static Dispatcher dispatcher = null;

    private DispatcherConfig dispatcherConfig;

    private HashMap<Method,ServiceMethod<Object, Object>> serviceMethodHashMap;
    private Dispatcher() {
        if (serviceMethodHashMap == null){
            serviceMethodHashMap = new HashMap<>();
        }
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
                invocationHandler);
    }

    private InvocationHandler invocationHandler = new InvocationHandler() {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {


            ServiceMethod<Object, Object> serviceMethod =  getServiceMethod(method);
            if (serviceMethod != null){
                return serviceMethod.getCallAdapter().getCall();
            }
            TransLog.e("invocationHandler : name  : " + method.getName());
            API api = method.getAnnotation(API.class);
            if (api == null) {
                Notice notice = method.getAnnotation(Notice.class);
                if (notice == null) {
                    throw new Exception("unSupport method");
                }
            }


            serviceMethod = new ServiceMethod<>(dispatcherConfig.getTransmitter(), method, args);
            CommonCall<Object> commonCall = new CommonCall<>(serviceMethod, args);
            TransCall<?> transCall = serviceMethod.getCallAdapter().adapt(commonCall);
            TransLog.e("transCall : " + transCall.getClass().getName());
            serviceMethodHashMap.put(method,serviceMethod);
            return transCall;
        }
    };

    private ServiceMethod<Object, Object> getServiceMethod(Method method){
        if (serviceMethodHashMap == null){
            return null;
        }
        if (serviceMethodHashMap.containsKey(method)){
            return serviceMethodHashMap.get(method);
        }
        return null;
    }
    public void receiverData(Message receivedData) {

        if (serviceMethodHashMap == null){
            return;
        }

        for (Map.Entry<Method, ServiceMethod<Object, Object>> serviceMethodEntry : serviceMethodHashMap.entrySet()) {
            ServiceMethod<Object, Object> serviceMethod = serviceMethodEntry.getValue();
            if (serviceMethod == null){
                continue;
            }

            Sender<?> sender = serviceMethod.getCommand();

            if (sender == null){
                return;
            }
            Object result = sender.handlerMessage(receivedData);
            TransLog.e("handler result : " + result);
            if (result != null){

                CommonCall<Object> commonCall = (CommonCall<Object>) serviceMethod.getCallAdapter().getCall();
                TransLog.e("commonCall : " + commonCall.getClass().getName());
                commonCall.onDataCallback(result,receivedData);
                serviceMethodHashMap.remove(serviceMethodEntry.getKey());
                break;
            }

        }


    }
}
