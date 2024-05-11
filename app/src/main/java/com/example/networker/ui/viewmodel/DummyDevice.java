package com.example.networker.ui.viewmodel;

import com.example.networker.database.model.device.DummyDeviceSettings;

import java.util.HashMap;
import java.util.Map;

public class DummyDevice extends Device {
    private DummyDeviceSettings settings;

    public DummyDevice() {
        this(null);
    }

    public DummyDevice(String name) {
        super("dummy://" + (name != null ? "<"+name+">/" : "ENOENT/"), name);
        setVendor("DummyDevices");

        settings = new DummyDeviceSettings();
    }

    @Override
    public HashMap<String,String> toMap() {
        HashMap<String,String> dataMap = new HashMap<>();

        dataMap.put("name", String.valueOf(getName()));
        return dataMap;
    }

    public static DummyDevice fromMap(Map<String,String> dataMap) {
        if (dataMap == null) {
            throw new IllegalArgumentException("dataMap cannot be null");
        }
        String name = dataMap.get("name");
        if (name == null) {
            throw new IllegalArgumentException("incompatible input dataMap");
        }

        DummyDevice device = new DummyDevice(name);
        return device;
    }

    public DummyDeviceSettings getSettings() {
        return settings;
    }

    public void setSettings(DummyDeviceSettings settings) {
        this.settings = settings;
    }

    public static DummyDevice fromModel(com.example.networker.database.model.device.DummyDevice device) {
        String name = device.getName();
        if (name == null || name.isEmpty()) {
            name = "Untitled";
        }
        DummyDevice viewmodelDevice = new DummyDevice(name);

        viewmodelDevice.setSettings(device.getSettings());
        return viewmodelDevice;
    }

    public com.example.networker.database.model.device.DummyDevice toModel() {
        com.example.networker.database.model.device.DummyDevice device = new com.example.networker.database.model.device.DummyDevice(getName());

        device.setSettings(getSettings());
        return device;
    }
}
