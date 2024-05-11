package com.example.networker.database.model.device;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class DeviceInterface {
    public enum Type {
        WAN, LAN
    }
    private String address = null;
    private int maskLength;
    private Type interfaceType = Type.WAN;

    public DeviceInterface() { }

    public DeviceInterface(String address, int maskLength, Type interfaceType) {
        if (address == null) {
            throw new IllegalArgumentException("address cannot be null");
        }
        this.address = address;
        this.maskLength = maskLength;
        this.interfaceType = interfaceType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMaskLength() {
        return maskLength;
    }

    public void setMaskLength(int maskLength) {
        this.maskLength = maskLength;
    }

    public Type getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Type interfaceType) {
        this.interfaceType = interfaceType;
    }
}
