package com.example.networker.database.model.device;

public class DummyDevice {
    private String name;
    private DummyDeviceSettings settings = new DummyDeviceSettings();

    public DummyDevice() {
    }

    public DummyDevice(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DummyDeviceSettings getSettings() {
        return settings;
    }

    public void setSettings(DummyDeviceSettings settings) {
        this.settings = settings;
    }
}
