package com.example.networker.ui.viewmodel;

import android.os.Bundle;
import android.os.PersistableBundle;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.annotations.Nullable;

public abstract class Device {
    private String vendor;
    private final String locationUri;
    private @Nullable Boolean connected = null;
    private @Nullable Boolean commandInitiated = null;
    private @Nullable String name = null;
    private Map<String, Object> properties = new HashMap<>();
    private Map<String, Object> mainFlowSettings = new HashMap<>();

    public Device(String locationUri) {
        this(locationUri, null);
    }

    public Device(String locationUri, String name) {
        if (locationUri == null) {
            throw new IllegalArgumentException("locationUri cannot be null");
        }

        this.locationUri = locationUri;
        this.name = name;
    }

    public static Device fromMap(Map<String,String> dataMap) {
        throw new RuntimeException("not implemented");
    }

    public abstract Map<String,String> toMap();


    public String getVendor() {
        return vendor;
    }

    public void setVendor(String customVendor) {
        if (customVendor == null) {
            throw new IllegalArgumentException("customVendor cannot be null");
        }
        this.vendor = customVendor;
    }

    public String getLocationUri() {
        return locationUri;
    }

    public @Nullable Boolean isConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public @Nullable Boolean isCommandInitiated() {
        return commandInitiated;
    }

    public void setCommandInitiated(Boolean commandInitiated) {
        this.commandInitiated = commandInitiated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
    
}
