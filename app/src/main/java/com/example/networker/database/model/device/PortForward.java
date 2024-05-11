package com.example.networker.database.model.device;

import java.net.Inet4Address;

public class PortForward {
    private int wanPort;
    private Inet4Address lanHost;
    private int lanHostPort;
    private boolean hairpinNAT;

    public PortForward() { }

    public PortForward(int wanPort, Inet4Address lanHost, int lanHostPort, boolean hairpinNAT) {
        this.wanPort = wanPort;
        this.lanHost = lanHost;
        this.lanHostPort = lanHostPort;
        this.hairpinNAT = hairpinNAT;
    }

    public int getWanPort() {
        return wanPort;
    }

    public void setWanPort(int wanPort) {
        this.wanPort = wanPort;
    }

    public Inet4Address getLanHost() {
        return lanHost;
    }

    public void setLanHost(Inet4Address lanHost) {
        this.lanHost = lanHost;
    }

    public int getLanHostPort() {
        return lanHostPort;
    }

    public void setLanHostPort(int lanHostPort) {
        this.lanHostPort = lanHostPort;
    }

    public boolean isHairpinNAT() {
        return hairpinNAT;
    }

    public void setHairpinNAT(boolean hairpinNAT) {
        this.hairpinNAT = hairpinNAT;
    }
}
