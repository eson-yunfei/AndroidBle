package com.e.tool.ble.imp;

import com.e.tool.ble.bean.ReadMessage;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/23 17:44
 * Package name : com.e.tool.ble.imp
 * Des :
 */
public interface OnRead {
   void onReadMessage(ReadMessage readMessage);
   void onReadError();
}
