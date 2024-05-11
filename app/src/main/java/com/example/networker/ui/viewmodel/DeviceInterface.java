package com.example.networker.ui.viewmodel;

import java.util.HashMap;
import java.util.Map;

public class DeviceInterface extends com.example.networker.database.model.device.DeviceInterface {
    public DeviceInterface() { }

    public DeviceInterface(String address, int maskLength, Type interfaceType) {
        super(address, maskLength, interfaceType);
    }

    public HashMap<String,String> toMap() {
        HashMap<String,String> dataMap = new HashMap<>();

        dataMap.put("type", String.valueOf(getInterfaceType()));
        dataMap.put("address", String.valueOf(getAddress()));
        dataMap.put("mask", String.valueOf(getMaskLength()));
        return dataMap;
    }

    public static DeviceInterface fromMap(Map<String,String> dataMap) {
        if (dataMap == null) {
            throw new IllegalArgumentException("dataMap cannot be null");
        }
        String ifType = dataMap.get("type");
        String address = dataMap.get("address");
        String mask = dataMap.get("mask");
        if (ifType == null || address == null || mask == null) {
            throw new IllegalArgumentException("incompatible input dataMap");
        }

        DeviceInterface device = new DeviceInterface(address, Integer.parseInt(mask), Type.valueOf(ifType));
        return device;
    }
}
