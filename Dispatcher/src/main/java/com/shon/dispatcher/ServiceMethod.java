package com.shon.dispatcher;

import com.shon.dispatcher.annotation.API;
import com.shon.dispatcher.annotation.Notice;
import com.shon.dispatcher.bean.Listener;
import com.shon.dispatcher.bean.Sender;
import com.shon.dispatcher.utils.TransLog;

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

    private Sender sender;

    private Listener listener;
    private CallAdapter<Object, TransCall<?>> callAdapter;

    ServiceMethod(Transmitter transmitter, Method method, Object[] args) {
        this.transmitter = transmitter;
        create(method, args);
    }

    Transmitter getTransmitter() {
        return transmitter;
    }

    Sender getCommand() {
        return sender;
    }

     Listener getListener() {
        return listener;
    }

    CallAdapter<Object, TransCall<?>> getCallAdapter() {
        return callAdapter;
    }

    private void create(Method method, Object[] args) {

        Type returnType = method.getGenericReturnType();
        Type type = Utils.getRawType(returnType);
        TransLog.e("invocationHandler : 返回类型 : " + type);
        Annotation[] annotations = method.getAnnotations();

        if (annotations.length == 0) {
            return;
        }
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                TransLog.e("args  : " + i + " ; type :" + arg.getClass());
            }
        }

        for (Annotation annotation : annotations) {
            TransLog.e("annotation : " + annotation.getClass());
            if (annotation instanceof API) {
                API api = method.getAnnotation(API.class);
                if (api == null) {
                    continue;
                }
                Class<? extends Sender> cls = api.name();
                try {
                    sender = cls.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (annotation instanceof Notice){
                Notice notice = method.getAnnotation(Notice.class);
                if (notice == null) {
                    continue;
                }
                Class<? extends Listener> cls = notice.name();
                try {
                    listener = cls.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        callAdapter = (CallAdapter<Object, TransCall<?>>) createCallAdapter(method);
    }


    private CallAdapter<Object, TransCall<?>> createCallAdapter(Method method) {
        final Type result = method.getGenericReturnType();
        return new CallAdapter<Object, TransCall<?>>() {
            private TransCall<Object> transCall;

            @Override
            public Type responseType() {
                return result;
            }

            @Override
            public TransCall<Object> adapt(TransCall<Object> call) {
                transCall = call;
                return transCall;
            }

            @Override
            public TransCall<Object> getCall() {
                return transCall;
            }
        };
    }

}
