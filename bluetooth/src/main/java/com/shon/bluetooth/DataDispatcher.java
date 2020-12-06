package com.shon.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.shon.bluetooth.core.Result;
import com.shon.bluetooth.core.call.BaseCall;
import com.shon.bluetooth.core.call.ICall;
import com.shon.bluetooth.core.call.Listener;
import com.shon.bluetooth.core.call.NotifyCall;
import com.shon.bluetooth.core.call.ReadCall;
import com.shon.bluetooth.core.call.WriteCall;
import com.shon.bluetooth.core.callback.ICallback;
import com.shon.bluetooth.core.callback.NotifyCallback;
import com.shon.bluetooth.core.callback.ReadCallback;
import com.shon.bluetooth.core.callback.WriteCallback;
import com.shon.bluetooth.util.BleLog;
import com.shon.bluetooth.util.ByteUtil;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 数据分发器
 */
public class DataDispatcher {
    private final Deque<ICall<?>> callDeque;
    private final List<Listener> listeners;
    private ICall<?> tempCall;
    public MutableLiveData<Result> resultMutableLiveData;

    private final Deque<Result> resultDeque;

    DataDispatcher() {
        listeners = new ArrayList<>();
        callDeque = new LinkedBlockingDeque<>();
        resultDeque = new LinkedBlockingDeque<>();
        resultMutableLiveData = new MutableLiveData<>();
        resultMutableLiveData.observeForever(result -> {
            resultDeque.add(result);
            handlerResult();
        });
    }


    public synchronized void startSendNext(boolean isFinish) {
        BleLog.d("startSendNext ++++" + isFinish + " ;   call = " + tempCall);
        if (isFinish) {
            tempCall = null;
            callDeque.removeFirst();

        }
        if (tempCall != null) {
            return;
        }
        tempCall = callDeque.peek();
        if (tempCall == null) {
            return;
        }

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (tempCall instanceof NotifyCall) {
            ((NotifyCall) tempCall).changeSate();
        }
        if (tempCall instanceof WriteCall) {
            ((WriteCall) tempCall).write();
        }
        if (tempCall instanceof ReadCall) {
            ((ReadCall) tempCall).startRead();
        }
    }

    public void onReceivedResult(Result value) {

        resultMutableLiveData.postValue(value);
    }

    public synchronized void enqueue(Listener listener) {
        listeners.add(listener);
    }

    public synchronized void enqueue(ICall<?> iCall) {
        callDeque.add(iCall);
        startSendNext(false);
    }

    private volatile Result tempResult;


    private synchronized void handlerResult() {
        if (tempResult != null) {
            return;
        }
        tempResult = resultDeque.peek();
        if (tempResult == null){
            return;
        }
        boolean finish = handlerResult(tempResult);
        if (finish){
            tempResult = null;
            resultDeque.removeFirst();
            handlerResult();
        }
    }

    private synchronized boolean handlerResult(Result result) {

        byte[] bytes = result.getBytes();
        String setValue = ByteUtil.getHexString(bytes);
        String address = result.getAddress();

        int type = result.getType();
        if (type == BluetoothGattCharacteristic.PROPERTY_WRITE) {
            WriteCallback writeCall = getWriteCallByWriteData(address, setValue);
            if (writeCall != null) {
                boolean isRemove = writeCall.removeOnWriteSuccess();
                if (isRemove) {
                    startSendNext(true);
                }
                return true;
            }

        }


        for (ICall<?> iCall : callDeque) {
            if (!TextUtils.equals(address, iCall.getAddress())) {
                continue;
            }

            if (iCall instanceof NotifyCall) {
                NotifyCallback callBack = (NotifyCallback) iCall.getCallBack();
                callBack.onChangeResult(true);
                ((NotifyCall) iCall).cancelTimer();
                startSendNext(true);
                break;
            }
            if (iCall instanceof ReadCall) {
                UUID uuid = ((ReadCall) iCall).getCharacteristicUUID();
                if (!TextUtils.equals(result.getUuid().toLowerCase(),
                        uuid.toString().toLowerCase())) {
                    continue;
                }
                ReadCallback readCallback = (ReadCallback) iCall.getCallBack();
                if (readCallback.process(address, bytes)) {
                    ((BaseCall<?, ?>) iCall).cancelTimer();
                    startSendNext(true);
                }
            }
            if (iCall instanceof WriteCall) {
                BleLog.d("DataDispatcher WriteCall : process " + type);
                WriteCallback writeCall = (WriteCallback) iCall.getCallBack();
                if (writeCall.process(address, bytes) && writeCall.isFinish()) {
                    ((BaseCall<?, ?>) iCall).cancelTimer();
                    startSendNext(true);
                }
            }
        }

        if (type == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
            for (Listener listener : listeners) {
                if (!TextUtils.equals(address, listener.getAddress())) {
                    continue;
                }

                ICallback callBack = listener.getCallBack();
                boolean process = callBack.process(address, bytes);
                if (process) {
                    break;
                }
            }
        }

        return true;

    }


    public WriteCallback getWriteCallByWriteData(String address, String writeData) {
        for (ICall<?> writer : callDeque) {
            if (!(writer instanceof WriteCall)) {
                continue;
            }
            if (!TextUtils.equals(address, writer.getAddress())) {
                continue;
            }
            WriteCallback writeCall = (WriteCallback) writer.getCallBack();
            byte[] writeInfo = writeCall.getSendData();
            if (TextUtils.equals(writeData, ByteUtil.getHexString(writeInfo))) {
                BleLog.d("找到发送的指令");
                return writeCall;
            }
        }
        return null;
    }

}
