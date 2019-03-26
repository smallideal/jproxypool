package com.smallideal.jproxypool.db;

import org.springframework.data.annotation.Id;

public class ProxyIp {
    //proxy ip
    @Id
    private String ip;
    //proxy ip port
    private int port;
    //proxy ip protocol : 1  means http ,  2 means https
    private int protocol;

    private long createTime;

    private long checkTime;
    //proxy type  1 means 高匿代理 , 0 means 未知代理类型
    private int type = Type.UK.getValue();


    private String address;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(long checkTime) {
        this.checkTime = checkTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public long getTtl(){
        return (checkTime-createTime)/1000;
    }

    public enum Type {
        GN(1), UK(0);
        private int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
