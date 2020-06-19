package com.shon.dispatcher;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 13:45
 * Package name : com.shon.dispatcher
 * Des :
 */
public class DispatcherConfig {
    private Class<?> serverInterface;
    private Transmitter transmitter;

    private DispatcherConfig() {

    }

    private <T> void setServerInterface(Class<T> server) {
        this.serverInterface = server;
    }

    public Class<?> getServerInterface() {
        return serverInterface;
    }

    public Transmitter getTransmitter() {
        return transmitter;
    }

    private void setTransmitter(Transmitter transmitter) {
        this.transmitter = transmitter;
    }

    @Override
    public String toString() {
        return "DispatcherConfig{" +
                "serverInterface=" + serverInterface.getName() +
                ", transmitter=" + transmitter.getClass().getName() +
                '}';
    }

    public static class Builder {
        private DispatcherConfig dispatcherConfig;

        public Builder() {
            dispatcherConfig = new DispatcherConfig();
        }


        public <T> Builder setApiInterface(Class<T> apiInterface) {
            dispatcherConfig.setServerInterface(apiInterface);
            return this;
        }

        public Builder setTransmitter(Transmitter transmitter){
            dispatcherConfig.setTransmitter(transmitter);
            return this;
        }
        public DispatcherConfig build() {
            return dispatcherConfig;
        }
    }
}
