package com.shon.dispatcher;

import android.os.Handler;
import android.os.Looper;

import com.shon.dispatcher.annotation.API;
import com.shon.dispatcher.annotation.Notice;
import com.shon.dispatcher.bean.Listener;
import com.shon.dispatcher.bean.Message;
import com.shon.dispatcher.bean.Sender;
import com.shon.dispatcher.call.ICall;
import com.shon.dispatcher.call.SenderCall;
import com.shon.dispatcher.utils.TransLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/24 15:24
 * Package name : com.shon.dispatcher
 * Des :
 */
class Invocation implements InvocationHandler {

    private HashMap<Method, ServiceMethod<Object, Object>> sendMethodMap;
    private HashMap<Method, ServiceMethod<Object, Object>> listenerMethodMap;
    private Transmitter transmitter;
    private ReadMessage readMessage;
    private TransRunnable transRunnable;
    private Handler handler;

    Invocation(Transmitter transmitter) {
        this.transmitter = transmitter;
        sendMethodMap = new HashMap<>();
        listenerMethodMap = new HashMap<>();

        readMessage = new ReadMessage(this);
        transRunnable = new TransRunnable();
        handler = new Handler(Looper.getMainLooper());
        ThreadPool  threadPool = ThreadPool.getThreadPool();
        threadPool.addTask(readMessage);
        threadPool.addTask(transRunnable);
    }

    public void addMessage(Message receivedData) {
        if (readMessage != null) {
            readMessage.addMessage(receivedData);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ServiceMethod<Object, Object> serviceMethod = getServiceMethod(method);
        if (serviceMethod != null) {
            return serviceMethod.getCallAdapter().getCall();
        }
        TransLog.e("invocationHandler : name  : " + method.getName());
        ICall<?> call = null;
        API api = method.getAnnotation(API.class);
        if (api == null) {
            Notice notice = method.getAnnotation(Notice.class);
            if (notice == null) {
                throw new Exception("unSupport method");
            }
        }else {

        }


        serviceMethod = new ServiceMethod<>(transmitter, method, args);
        CommonCall<Object> commonCall = new CommonCall<>(serviceMethod, args, handler, transRunnable);
        SenderCall<?> transCall = (SenderCall<?>) serviceMethod.getCallAdapter().adapt(commonCall);
        TransLog.e("transCall : " + transCall.getClass().getName());
        sendMethodMap.put(method, serviceMethod);
        return transCall;
    }

    private ServiceMethod<Object, Object> getServiceMethod(Method method) {
        if (sendMethodMap == null) {
            return null;
        }
        if (sendMethodMap.containsKey(method)) {
            return sendMethodMap.get(method);
        }
        return null;
    }


    public void handlerMessage(Message message) {
        for (Map.Entry<Method, ServiceMethod<Object, Object>> serviceMethodEntry : sendMethodMap.entrySet()) {
            ServiceMethod<Object, Object> serviceMethod = serviceMethodEntry.getValue();
            if (serviceMethod == null) {
                continue;
            }
            Object result = null;
            Sender<?> sender = serviceMethod.getCommand();
            if (sender == null) {
                Listener<?> listener = serviceMethod.getListener();
                if (listener == null) {
                    continue;
                }
                result = listener.handlerMessage(message);
            } else {
                result = sender.handlerMessage(message);
            }


            if (result == null) {
                return;
            }

            CommonCall<Object> commonCall = (CommonCall<Object>) serviceMethod.getCallAdapter().getCall();
            TransLog.e("commonCall : " + commonCall.getClass().getName());
            commonCall.onDataCallback(result, message);
            commonCall.cancelTimeOut();


        }
    }


}
