package com.shon.dispatcher.core;

import com.shon.dispatcher.Transmitter;
import com.shon.dispatcher.annotation.API;
import com.shon.dispatcher.annotation.Notice;
import com.shon.dispatcher.call.CallAdapter;
import com.shon.dispatcher.call.ICall;
import com.shon.dispatcher.command.Listener;
import com.shon.dispatcher.command.Sender;
import com.shon.dispatcher.utils.TransLog;
import com.shon.dispatcher.utils.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:39
 * Package name : com.shon.dispatcher.core
 * Des :
 */
class ServiceMethod<RequestT, ResultT> {

    private Transmitter transmitter;

    private Sender<?> sender;

    private Listener<?> listener;
    private CallAdapter<Object, ICall<?>> callAdapter;

    ServiceMethod(Transmitter transmitter, Method method) {
        this.transmitter = transmitter;
        create(method);
    }

    Transmitter getTransmitter() {
        return transmitter;
    }

    Sender<?> getCommand() {
        return sender;
    }

    Listener<?> getListener() {
        return listener;
    }

    CallAdapter<Object, ICall<?>> getCallAdapter() {
        return callAdapter;
    }

    private void create(Method method) {
        Type returnType = method.getGenericReturnType();
        Type type = Utils.getRawType(returnType);
        TransLog.e("invocationHandler : 返回类型 : " + type);
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            TransLog.e("annotation : " + annotation.getClass());
            if (annotation instanceof API) {
                createCall(method);
            }
            if (annotation instanceof Notice) {
                createListener(method);
            }
        }
        callAdapter = (CallAdapter<Object, ICall<?>>) createCallAdapter(method);

    }

    private void createCall(Method method) {

        API api = method.getAnnotation(API.class);
        if (api == null) {
            return;
        }
        Class<? extends Sender> cls = api.name();
        try {
            sender = cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createListener(Method method) {
        Notice notice = method.getAnnotation(Notice.class);
        if (notice == null) {
            return;
        }
        Class<? extends Listener> cls = notice.name();
        try {
            listener = cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CallAdapter<Object, ICall<?>> createCallAdapter(Method method) {
        final Type result = method.getGenericReturnType();
        return new CallAdapter<Object, ICall<?>>() {
            private ICall<Object> transCall;

            @Override
            public Type responseType() {
                return result;
            }

            @Override
            public ICall<Object> adapt(ICall<Object> call) {
                transCall = call;
                return transCall;
            }

            @Override
            public ICall<Object> getCall() {
                return transCall;
            }
        };
    }

}
