package com.shon.dispatcher.core;

import android.os.Handler;
import android.os.Looper;

import com.shon.dispatcher.InvocationHandler;
import com.shon.dispatcher.Transmitter;
import com.shon.dispatcher.annotation.API;
import com.shon.dispatcher.annotation.Notice;
import com.shon.dispatcher.TMessage;
import com.shon.dispatcher.call.ICall;
import com.shon.dispatcher.call.SenderCall;
import com.shon.dispatcher.command.Listener;
import com.shon.dispatcher.command.Sender;
import com.shon.dispatcher.utils.TransLog;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/24 15:24
 * Package name : com.shon.dispatcher
 * Des :
 */
class Invocation extends InvocationHandler {

    private HashMap<Method, ServiceMethod<Object, Object>> sendMethodMap;
    private HashMap<Method, ServiceMethod<Object, Object>> listenerMethodMap;
    private Transmitter transmitter;
    private ReadMessage readMessage;
    private SendRunnable sendRunnable;
    private Handler handler;

    Invocation(Transmitter transmitter) {
        this.transmitter = transmitter;
        sendMethodMap = new HashMap<>();
        listenerMethodMap = new HashMap<>();

        readMessage = new ReadMessage(this);
        sendRunnable = new SendRunnable();
        handler = new Handler(Looper.getMainLooper());
        ThreadPool threadPool = ThreadPool.getThreadPool();
        threadPool.addTask(readMessage);
        threadPool.addTask(sendRunnable);
    }

    public void addMessage(TMessage receivedData) {
        if (readMessage != null) {
            readMessage.addMessage(receivedData);
        }
    }

    public void sendSuccess(TMessage TMessage) {
        sendRunnable.sendSuccess(TMessage);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ServiceMethod<Object, Object> serviceMethod = getServiceMethod(method);
        if (serviceMethod != null) {

            ICall<?> iCall = serviceMethod.getCallAdapter().getCall();
            TransLog.e("transCall : " + iCall.getClass().getName());
            return iCall;
        }
        TransLog.e("invocationHandler : name  : " + method.getName());
        ICall<?> call = null;
        API api = method.getAnnotation(API.class);
        if (api == null) {
            Notice notice = method.getAnnotation(Notice.class);
            if (notice == null) {
                throw new Exception("unSupport method");
            }

            serviceMethod = new ServiceMethod<>(transmitter, method);
            CommListenerCall<Object> commonCall = new CommListenerCall<>(handler);
            call = (ICall<?>) serviceMethod.getCallAdapter().adapt(commonCall);

        } else {
            serviceMethod = new ServiceMethod<>(transmitter, method);
            CommonCall<Object> commonCall = new CommonCall<>(serviceMethod, args, handler, sendRunnable);
            call = (ICall<?>) serviceMethod.getCallAdapter().adapt(commonCall);
        }

        if (args != null && args.length != 0) {
            for (Object arg : args) {
                TransLog.e("arg : " + arg.getClass().getName());
            }
        }


        TransLog.e("transCall : " + call.getClass().getName());
        sendMethodMap.put(method, serviceMethod);
        return call;
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


    public void handlerMessage(TMessage TMessage) {
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
                result = listener.handlerMessage(TMessage);
            } else {
                result = sender.handlerMessage(TMessage);
            }


            if (result == null) {
                return;
            }

            ICall<Object> commonCall = (ICall<Object>) serviceMethod.getCallAdapter().getCall();
            TransLog.e("onDataCallback : " + TMessage.toString());
            commonCall.onDataCallback(result, TMessage);

            if (commonCall instanceof SenderCall<?>) {
                ((SenderCall<?>) commonCall).cancelTimeOut();
                sendMethodMap.remove(serviceMethodEntry.getKey());
            }

        }
    }


}
