package com.example.networker;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.networker.database.domain.DummyDeviceBackend;
import com.example.networker.ui.viewmodel.DeviceInterface;
import com.example.networker.ui.viewmodel.DummyDevice;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;

public class DeviceDetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG = DeviceDetailsActivity.class.getName();

    ViewPager2 viewPager;
    TabLayout tabChooser;

    private int deviceMonitorListGridNumber = 1;
    private DummyDeviceBackend dbBackend;

    DeviceDetailsActivityTabbing tabAdapter;
    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        viewPager = findViewById(R.id.viewpager);
        tabChooser = findViewById(R.id.tabChooser);

        dbBackend = new DummyDeviceBackend();

        if (savedInstanceState != null) {
            deviceId = savedInstanceState.getString(MainActivity.DEVICEDETAILS_PARAM_DEVICE_ID);
        }
        else {
            deviceId = getIntent().getStringExtra(MainActivity.DEVICEDETAILS_PARAM_DEVICE_ID);
        }

        //Serializable deviceSerializable = getIntent().getSerializableExtra(MainActivity.DEVICEDETAILS_PARAM_DEVICE);
        //if (deviceSerializable != null) {
        if (deviceId != null) {
            //dummyDevice = DummyDevice.fromMap((HashMap) deviceSerializable);
            dbBackend.getUserDevice(MainActivity.getDbCurrentUser().getId(), deviceId).subscribe(
                    devicePair -> {
                        DummyDevice dummyDevice = DummyDevice.fromModel(devicePair.second);

                        Log.i(LOG_TAG, String.valueOf(dummyDevice));
                        if (dummyDevice != null) {
                            Log.i(LOG_TAG, "1" + String.valueOf(dummyDevice.getSettings()));
                            if (dummyDevice.getSettings() != null) {
                                Log.i(LOG_TAG, "2" + String.valueOf(dummyDevice.getSettings().getInterfaceList()));
                                if (dummyDevice.getSettings().getInterfaceList() != null) {
                                    for (com.example.networker.database.model.device.DeviceInterface deviceInterface : dummyDevice.getSettings().getInterfaceList()) {
                                        Log.i(LOG_TAG, "3" + String.valueOf(deviceInterface));
                                        if (deviceInterface != null) {
                                            Log.i(LOG_TAG, "4" + String.valueOf(deviceInterface.getAddress()));
                                        }
                                    }
                                }
                            }
                        }

                        tabAdapter = new DeviceDetailsActivityTabbing(this, dummyDevice);
                        setupFragments();
                    }
            );
        }
        else {
            tabAdapter = new DeviceDetailsActivityTabbing(this);
            setupFragments();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MainActivity.DEVICEDETAILS_PARAM_DEVICE_ID, deviceId);
    }

    private void setupFragments() {
        viewPager.setAdapter(tabAdapter);
        new TabLayoutMediator(tabChooser, viewPager,
                (tab, position) -> tab.setText(tabAdapter.getTabNames().get(position))
        ).attach();

        // DummyDeviceDetailsFragment
        getSupportFragmentManager().setFragmentResultListener(
                DummyDeviceDetailsFragment.RESULT_DEVICE,
                this,
                (requestKey, bundle) -> {
                    HashMap<String,String> deviceMap = (HashMap<String, String>) bundle.getSerializable(DummyDeviceDetailsFragment.RESULT_DEVICE);

                    saveDevice(DummyDevice.fromMap(deviceMap));
                    finish();
                }
        );

        // DummyDeviceSettingsFragment
        getSupportFragmentManager().setFragmentResultListener(
                DummyDeviceSettingsFragment.RESULT_INTERFACE,
                this,
                (requestKey, bundle) -> {
                    HashMap<String,String> interfaceMap = (HashMap<String, String>) bundle.getSerializable(DummyDeviceSettingsFragment.RESULT_INTERFACE);

                    addNewInterface(DeviceInterface.fromMap(interfaceMap));
                }
        );
        getSupportFragmentManager().setFragmentResultListener(
                DummyDeviceSettingsFragment.DELETE_INTERFACE,
                this,
                (requestKey, bundle) -> {
                    int interfacePos = bundle.getInt(DummyDeviceSettingsFragment.DELETE_INTERFACE);
                    deleteInterface(interfacePos);
                }
        );
    }

    protected void saveDevice(DummyDevice device) {
        dbBackend.setUserDevice(MainActivity.getDbCurrentUser().getId(), deviceId, device.toModel());
    }

    protected void addNewInterface(com.example.networker.database.model.device.DeviceInterface deviceInterface) {
        dbBackend.addInterface(MainActivity.getDbCurrentUser().getId(), deviceId, deviceInterface);
    }

    protected void deleteInterface(int position) {
        dbBackend.deleteInterface(MainActivity.getDbCurrentUser().getId(), deviceId, position);
    }
}