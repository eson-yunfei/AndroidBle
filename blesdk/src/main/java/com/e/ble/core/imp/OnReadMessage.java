package com.e.ble.core.imp;

import com.e.ble.core.bean.ReadMessage;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 17:44
 * Package name : com.e.ble.core.imp
 * Des :
 */
public interface OnReadMessage {
   void onReadMessage(ReadMessage readMessage);
   void onReadError();
}
