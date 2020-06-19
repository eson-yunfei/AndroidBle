package org.eson.liteble.command;

import com.shon.dispatcher.bean.BaseCommand;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 11:26
 * Package name : org.eson.liteble.command
 * Des :
 */
public class SendCmd extends BaseCommand {
    @Override
    public byte[] getSendCmd() {
        return new byte[]{1,2,3,4,5};
    }
}
