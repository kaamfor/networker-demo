package com.example.networker.database.domain;

import android.util.Log;
import android.util.Pair;

import com.example.networker.database.model.device.DeviceInterface;
import com.example.networker.database.model.device.DummyDevice;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.ReplaySubject;

public class DummyDeviceBackend {
    private static final String LOG_TAG = DummyDeviceBackend.class.getName();

    public FirebaseFirestore getDatabaseRoot() {
        return FirebaseFirestore.getInstance();
    }

    public CollectionReference getUserList() {
        return getDatabaseRoot().collection("users");
    }
    public DocumentReference getUserBase(String userId) {
        return getUserList().document(userId);
    }

    public CollectionReference getUserDevicesListBase(String userId) {
        return getUserBase(userId).collection("devices");
    }

    public DocumentReference getUserDeviceBase(String userId, String deviceId) {
        return getUserDevicesListBase(userId).document(deviceId);
    }

    public Observable<Pair<String, DummyDevice>> getUserDevice(String userId, String deviceId) {
        DocumentReference reference = getUserDeviceBase(userId, deviceId);
        ReplaySubject<Pair<String, DummyDevice>> obs = ReplaySubject.create();

        reference.get().addOnSuccessListener(documentSnapshot -> {

            Map<String, Object> data = documentSnapshot.getData();
            Log.i(LOG_TAG, "5" + String.valueOf(data));
            if (data != null) {
                Object data_settings = data.getOrDefault("settings", null);
                Log.i(LOG_TAG, "6" + String.valueOf(data_settings));
                if (data_settings != null && data_settings instanceof Map) {
                    Object interfaceList = ((Map<?, ?>) data_settings).get("interfaceList");
                    Log.i(LOG_TAG, "7" + String.valueOf(interfaceList));
                    if (interfaceList != null && interfaceList instanceof List) {
                        ((List)interfaceList).forEach(devIf -> Log.i(LOG_TAG, "8" + String.valueOf(devIf)));

                        //((List)interfaceList).forEach((k, devIf) -> Log.i(LOG_TAG, "4" + String.valueOf(devIf)));
                    }
                }
            }

            DummyDevice device = documentSnapshot.toObject(DummyDevice.class);

            Log.i(LOG_TAG, "9" + String.valueOf(device.getSettings().getInterfaceList()));
            device.getSettings().getInterfaceList().forEach(devIf -> Log.i(LOG_TAG, "10" + String.valueOf(devIf.getAddress())));

            if (!documentSnapshot.exists() || device == null) {
                obs.onError(new RuntimeException("Device not exists or cannot be loaded"));
                return;
            }
            obs.onNext(new Pair<>(documentSnapshot.getId(), device));
            obs.onComplete();
        });

        return obs;
    }

    public Observable<Pair<String, DummyDevice>> fetchAllUserDevices(String userId) {
        CollectionReference reference = getUserDevicesListBase(userId);
        ReplaySubject<Pair<String, DummyDevice>> obs = ReplaySubject.create();

        reference.get().addOnSuccessListener(documentSnapshots -> {
            for (DocumentSnapshot snapshot: documentSnapshots.getDocuments()) {
                if (snapshot != null && snapshot.exists()) {
                    DummyDevice device = snapshot.toObject(DummyDevice.class);
                    if (device != null) {
                        obs.onNext(new Pair<>(snapshot.getId(), device));
                    }
                }
            }
            obs.onComplete();
        });

        return obs;
    }

    public Observable<List<Pair<String, DummyDevice>>> fetchAllUserDevicesRealtime(String userId) {
        CollectionReference reference = getUserDevicesListBase(userId);
        ReplaySubject<List<Pair<String, DummyDevice>>> obs = ReplaySubject.create();

        reference.addSnapshotListener(
                (value, error) -> {
                    if (error == null && !value.isEmpty()) {
                        List<Pair<String, DummyDevice>> result = new LinkedList<>();
                        for (DocumentSnapshot snapshot: value.getDocuments()) {
                            if (snapshot != null && snapshot.exists()) {
                                result.add(new Pair<>(snapshot.getId(), snapshot.toObject(DummyDevice.class)));
                            }
                        }
                        obs.onNext(result);
                    }
                }
        );

        return obs;
    }

    public String makeDeviceId(String userId) {
        return getUserDevicesListBase(userId).document().getId();
    }

    public Task<Void> addUserDevice(String userId, DummyDevice device) {
        return getUserDevicesListBase(userId).document().set(device);
    }

    public void addInterface(String userId, String deviceId, DeviceInterface deviceInterface) {
        getUserDevice(userId, deviceId).subscribe(
                device -> {
                    DummyDevice deviceInst = device.second;
                    deviceInst.getSettings().getInterfaceList().add(deviceInterface);
                    setUserDevice(userId, deviceId, deviceInst);
                }
        );
    }

    public Task<Void> setUserDevice(String userId, String deviceId, DummyDevice device) {
        return getUserDevicesListBase(userId).document(deviceId).set(device);
    }

    public void setInterface(String userId, String deviceId, int interfaceNo, DeviceInterface deviceInterface) {
        getUserDevice(userId, deviceId).subscribe(
                device -> {
                    DummyDevice deviceInst = device.second;
                    deviceInst.getSettings().getInterfaceList().set(interfaceNo, deviceInterface);
                    setUserDevice(userId, deviceId, deviceInst);
                }
        );
    }

    public Task<Void> deleteUserDevice(String userId, String deviceId) {
        return getUserDevicesListBase(userId).document(deviceId).delete();
    }

    public void deleteInterface(String userId, String deviceId, int interfaceNo) {
        getUserDevice(userId, deviceId).subscribe(
                device -> {
                    DummyDevice deviceInst = device.second;
                    deviceInst.getSettings().getInterfaceList().remove(interfaceNo);
                    setUserDevice(userId, deviceId, deviceInst);
                }
        );
    }
}
