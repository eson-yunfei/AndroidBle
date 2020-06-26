package com.shon.dispatcher;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/19 17:10
 * Package name : com.shon.dispatcher.bean
 * Des : 数据的实体
 */
public class TMessage {

    private byte[] bytes;  // 数据
    private String tag;   // 扩展字段，用户字段实现

    private Object object; //扩展字段，用户字段实现

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "TMessage{" +
                "bytes=" + getHexString(bytes) +
                ", tag=" + tag +
                ", object=" + object +
                '}';
    }


    /**
     * 十六进制打印数组
     */
    private String getHexString(byte[] buffer) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (byte b : buffer) {
            String intS = Integer.toHexString(b & 0xff);
            if (intS.length() == 1) {
                sb.append("  ").append("0").append(intS);
            } else {
                sb.append("  ").append(intS);
            }
        }
        sb.append("  ]");
        return sb.toString();
    }

}
