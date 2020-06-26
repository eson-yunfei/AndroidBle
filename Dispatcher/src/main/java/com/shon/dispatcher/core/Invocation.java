package com.shon.dispatcher.core;

import android.os.Handler;
import android.os.Looper;

import com.shon.dispatcher.AbsInvocation;
import com.shon.dispatcher.Transmitter;
import com.shon.dispatcher.annotation.Writer;
import com.shon.dispatcher.annotation.Notice;
import com.shon.dispatcher.TMessage;
import com.shon.dispatcher.call.ICall;
import com.shon.dispatcher.call.SenderCall;
import com.shon.dispatcher.transer.Listener;
import com.shon.dispatcher.transer.Sender;
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
class Invocation extends AbsInvocation {

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

    public void sendSuccess(TMessage tMessage) {
        if (sendRunnable != null) {
            sendRunnable.sendSuccess(tMessage);
        }
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
        Writer writer = method.getAnnotation(Writer.class);
        if (writer == null) {
            Notice notice = method.getAnnotation(Notice.class);
            if (notice == null) {
                throw new Exception("unSupport method");
            }

            serviceMethod = new ServiceMethod<>(transmitter, method);
            CommListenerCall<Object> commonCall = new CommListenerCall<>(handler);
            call = serviceMethod.getCallAdapter().adapt(commonCall);

        } else {
            serviceMethod = new ServiceMethod<>(transmitter, method);
            CommonCall<Object> commonCall = new CommonCall<>(serviceMethod, args, handler, sendRunnable);
            call = serviceMethod.getCallAdapter().adapt(commonCall);
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


    public void handlerMessage(TMessage tMessage) {
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
                result = listener.handlerMessage(tMessage);
            } else {
                result = sender.handlerMessage(tMessage);
            }


            if (result == null) {
                return;
            }

            ICall<Object> commonCall = serviceMethod.getCallAdapter().getCall();
//            TransLog.e("onDataCallback : " + tMessage.toString());
            commonCall.onDataCallback(result, tMessage);

            if (commonCall instanceof SenderCall<?>) {
                ((SenderCall<?>) commonCall).cancelTimeOut();
                sendMethodMap.remove(serviceMethodEntry.getKey());
            }

        }
    }


}
