package com.shon.dispatcher.core;

import com.shon.dispatcher.TransCall;
import com.shon.dispatcher.Transmitter;
import com.shon.dispatcher.annotation.API;
import com.shon.dispatcher.bean.BaseCommand;
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
public class ServiceMethod<RequestT, ResultT> {

    private Transmitter transmitter;

    private BaseCommand baseCommand;

    private CallAdapter<Object, TransCall<?>> callAdapter;

    public ServiceMethod(Transmitter transmitter, Method method, Object[] args) {
        this.transmitter = transmitter;
        create(method, args);
    }

    public Transmitter getTransmitter() {
        return transmitter;
    }

    public BaseCommand getCommand() {
        return baseCommand;
    }

    public CallAdapter<Object, TransCall<?>> getCallAdapter() {
        return callAdapter;
    }

    private void create(Method method,Object[] args) {

        Type returnType = method.getGenericReturnType();
        Type type = Utils.getRawType(returnType);
        TransLog.e("invocationHandler : 返回类型 : " + type);
        Annotation[] annotations = method.getAnnotations();

        if (annotations.length == 0) {
            return;
        }
        if (args != null){
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                TransLog.e("args  : " +i + " ; type :" + arg.getClass());
            }
        }

        for (Annotation annotation : annotations) {
            TransLog.e("annotation : " + annotation.getClass());
            if (annotation instanceof API) {
                API api = method.getAnnotation(API.class);
                if (api == null) {
                    continue;
                }
                Class<? extends BaseCommand> cls = api.name();
                try {
                    baseCommand = cls.newInstance();
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

            @Override
            public Type responseType() {
                return result;
            }

            @Override
            public TransCall<Object> adapt(TransCall<Object> call) {
                return call;
            }
        };
    }

}
