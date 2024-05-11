package com.example.networker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.networker.ui.viewmodel.DummyDevice;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class DeviceDetailsActivityTabbing extends FragmentStateAdapter {
    @NonNull
    private final FragmentActivity activity;
    private final DummyDevice device;
    private static final String[] tabNames = new String[] {
            "Connection", "Settings", "Monitor"
    };
    private static final Function<DummyDevice, Fragment>[] fragmentClasses = new Function[] {
            device -> new DummyDeviceDetailsFragment((DummyDevice) device),
            device -> new DummyDeviceSettingsFragment((DummyDevice) device),
            device -> new DummyDeviceGraphsFragment((DummyDevice) device)
    };

    public DeviceDetailsActivityTabbing(@NonNull FragmentActivity fragmentActivity, DummyDevice device) {
        super(fragmentActivity);
        activity = fragmentActivity;
        this.device = device;
    }

    public DeviceDetailsActivityTabbing(@NonNull FragmentActivity fragmentActivity) {
        this(fragmentActivity, new DummyDevice());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentClasses[position].apply(device);
    }

    @Override
    public int getItemCount() {
        return Math.min(tabNames.length, fragmentClasses.length);
    }

    public List<String> getTabNames() {
        return Arrays.asList(tabNames);
    }
}
