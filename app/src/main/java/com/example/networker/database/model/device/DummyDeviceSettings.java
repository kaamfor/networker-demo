package com.example.networker.database.model.device;

import com.example.networker.database.model.device.DeviceInterface;

import java.util.ArrayList;
import java.util.List;

public class DummyDeviceSettings {
    private List<DeviceInterface> interfaceList = new ArrayList<>();
    private List<PortForward> portForwards = new ArrayList<>();

    public List<DeviceInterface> getInterfaceList() {
        return interfaceList;
    }

    public void setInterfaceList(List<DeviceInterface> interfaceList) {
        this.interfaceList = interfaceList;
    }

    public List<PortForward> getPortForwards() {
        return portForwards;
    }

    public void setPortForwards(List<PortForward> portForwards) {
        this.portForwards = portForwards;
    }
}
