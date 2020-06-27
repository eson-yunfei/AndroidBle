package com.e.tool.ble.imp;

import com.e.tool.ble.bean.message.NotifyState;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/25 16:56
 * Package name : com.e.tool.ble.imp
 * Des :
 */
public interface OnWriteDescriptor {
     void onWriteDescriptorError() ;

     void onWriteDescriptor(NotifyState result) ;
}
