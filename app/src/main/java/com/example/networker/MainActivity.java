package com.example.networker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.networker.database.domain.DummyDeviceBackend;
import com.example.networker.database.model.device.DummyDevice;
import com.example.networker.ui.adapter.DeviceChooser;
import com.example.networker.ui.viewmodel.Device;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private class OwnDeviceChooser extends DeviceChooser {
        public OwnDeviceChooser(Context context, RecyclerView deviceSelectorList) {
            super(context, deviceSelectorList);
        }

        @Override
        public void onBindViewHolder(@NonNull DeviceChooser.ViewHolder holder, int position) {
            Device device = getFilteredDeviceList().get(position);
            if (device instanceof com.example.networker.ui.viewmodel.DummyDevice) {
                com.example.networker.ui.viewmodel.DummyDevice dummyDevice = (com.example.networker.ui.viewmodel.DummyDevice) device;

                holder.itemView.setOnClickListener(null);
                holder.itemView.setOnClickListener(
                        l -> showDummyDeviceDetails(dummyDevice)
                );
                holder.itemView.setOnLongClickListener(
                        v -> {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Delete device")
                                    .setMessage("Are you sure you want to delete device '"
                                            + String.valueOf(dummyDevice.getName()) +"'?")
                                    .setPositiveButton(android.R.string.yes,
                                            (dialog, which) -> {
                                                if (deviceIds.containsKey(dummyDevice)) {
                                                    dbBackend.deleteUserDevice(dbCurrentUser.getId(), deviceIds.get(dummyDevice));
                                                    deviceChooserAdapter.deleteDevice(dummyDevice);
                                                }
                                            })
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            return true;
                        }
                );
            }
            super.onBindViewHolder(holder, position);
        }
    }

    private static final String LOG_TAG = MainActivity.class.getName();
    public static final String DEVICEDETAILS_PARAM_DEVICE = "device";
    public static final String DEVICEDETAILS_PARAM_DEVICE_ID = "device_id";

    RecyclerView deviceSelectorList;
    DeviceChooser deviceChooserAdapter;

    private final DummyDeviceBackend dbBackend = new DummyDeviceBackend();
    private DocumentReference dbCurrentUser;
    private static Map<Device, String> deviceIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbCurrentUser = getDbCurrentUser();
        if (dbCurrentUser == null) {
            finish();
            return;
        }
        deviceIds = new HashMap<>();

        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        initDeviceSelector();
    }

    private void initDeviceSelector() {
        deviceSelectorList = findViewById(R.id.deviceSelectorList);
        deviceSelectorList.setLayoutManager(new GridLayoutManager(this, 1));

        deviceChooserAdapter = new OwnDeviceChooser(this, deviceSelectorList);
        populateDeviceList();

        findViewById(R.id.addNewDeviceBtn).setOnClickListener(
                view -> showNewDeviceScreen()
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }


    public void showNewDeviceScreen() {
        DialogFragment dialogFragment = new DummyDeviceCreatorDialog();
        dialogFragment.show(getSupportFragmentManager(), MainActivity.class.getName());

        getSupportFragmentManager().setFragmentResultListener(
                DummyDeviceCreatorDialog.RESULT_DUMMYDEVICE,
                this,
                (requestKey, bundle) -> {
                    HashMap<String,String> dummyDeviceMap = (HashMap<String, String>) bundle.getSerializable(DummyDeviceCreatorDialog.RESULT_DUMMYDEVICE);

                    com.example.networker.ui.viewmodel.DummyDevice device = com.example.networker.ui.viewmodel.DummyDevice.fromMap(dummyDeviceMap);

                    String deviceId = addDevice(device);
                    deviceIds.put(device, deviceId);
                    deviceChooserAdapter.addDevice(device);

                    dialogFragment.dismiss();
                    showDummyDeviceDetails(device);
                }
        );
    }

    public void showDummyDeviceDetails(com.example.networker.ui.viewmodel.DummyDevice device) {
        Intent dst = new Intent(this, DeviceDetailsActivity.class);

        dst.putExtra(DEVICEDETAILS_PARAM_DEVICE, device.toMap());
        dst.putExtra(DEVICEDETAILS_PARAM_DEVICE_ID, deviceIds.get(device));
        startActivity(dst);
    }

    public String addDevice(com.example.networker.ui.viewmodel.DummyDevice device) {
        DummyDevice deviceModel = device.toModel();

        String deviceId = dbBackend.makeDeviceId(dbCurrentUser.getId());
        dbBackend.setUserDevice(dbCurrentUser.getId(), deviceId, deviceModel);
        return deviceId;
    }

    public void populateDeviceList() {
        //dbBackend.setUserDevice(dbCurrentUser.getId(), dbBackend.makeDeviceId(dbCurrentUser.getId()), new DummyDevice("akarmi"));
        if (dbCurrentUser != null && deviceChooserAdapter != null) {
            String userId = dbCurrentUser.getId();
            dbBackend.fetchAllUserDevicesRealtime(userId).subscribe(
                    listOfPairs -> {
                        com.example.networker.ui.viewmodel.DummyDevice device;
                        deviceChooserAdapter.clear();

                        for (Pair<String, DummyDevice> devicePair : listOfPairs) {
                            device = com.example.networker.ui.viewmodel.DummyDevice.fromModel(devicePair.second);

                            deviceIds.put(device, devicePair.first);
                            deviceChooserAdapter.addDevice(device);
                        }
                    }
            );
        }
    }

    public static DocumentReference getDbCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return null;
        }
        return new DummyDeviceBackend().getUserBase(user.getUid());
    }
    public static String getDeviceId(Device device) {
        return deviceIds.getOrDefault(device, null);
    }
}