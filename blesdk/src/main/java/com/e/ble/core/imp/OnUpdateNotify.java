package com.e.ble.core.imp;

import com.e.ble.core.bean.NotifyState;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 16:56
 * Package name : com.e.ble.core.imp
 * Des :
 */
public interface OnUpdateNotify {
     void onWriteDescriptorError() ;

     void onWriteDescriptor(NotifyState result) ;
}
