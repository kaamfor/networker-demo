package com.example.networker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.networker.database.model.device.DeviceInterface;
import com.example.networker.ui.adapter.DeviceInterfacesList;
import com.example.networker.ui.viewmodel.DummyDevice;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;


public class DummyDeviceSettingsFragment extends Fragment {
    private class OwnDeviceInterfacesList extends DeviceInterfacesList {
        public OwnDeviceInterfacesList(Context context, RecyclerView interfaceSelectorList) {
            super(context, interfaceSelectorList);
        }

        @Override
        public void onBindViewHolder(@NonNull DeviceInterfacesList.ViewHolder holder, int position) {
            DeviceInterface deviceIf = getFilteredInterfaceList().get(position);
            if (deviceIf != null) {
                holder.itemView.setOnLongClickListener(
                        v -> {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Delete interface")
                                    .setMessage("Are you sure you want to delete interface with IP '"
                                            + String.valueOf(deviceIf.getAddress()) +"'?")
                                    .setPositiveButton(android.R.string.yes,
                                            (dialog, which) -> {
                                                DummyDeviceSettingsFragment.this.deleteInterface(position);
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

    private static final String LOG_TAG = DummyDeviceSettingsFragment.class.getName();
    public static final String RESULT_INTERFACE = DummyDeviceSettingsFragment.class.getName() + "_interface";
    public static final String DELETE_INTERFACE = DummyDeviceSettingsFragment.class.getName() + "_del_interface";
    private DummyDevice device;
    private RecyclerView settingListView;
    private FloatingActionButton addSettingBtn;

    private DeviceInterfacesList interfacesList;
    private final int settingListViewGridNumber = 1;

    public DummyDeviceSettingsFragment() {
        super();
        device = null;
    }

    public DummyDeviceSettingsFragment(DummyDevice device) {
        super();
        this.device = device;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dummydevice_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingListView = getView().findViewById(R.id.setting_list);
        addSettingBtn = getView().findViewById(R.id.add_setting_btn);

        settingListView.setLayoutManager(new GridLayoutManager(view.getContext(), settingListViewGridNumber));
        interfacesList = new OwnDeviceInterfacesList(getActivity(), settingListView);

        addSettingBtn.setOnClickListener(l -> showNewInterfaceDialog());

        bindToDevice();
    }

    public void bindToDevice() {
        if (device != null) {
            device.getSettings().getInterfaceList().forEach(interfacesList::addInterface);
        }
    }

    public void showNewInterfaceDialog() {
        DialogFragment dialogFragment = new DummyDeviceInterfaceDialog();
        dialogFragment.show(getActivity().getSupportFragmentManager(), getActivity().getClass().getName());

        getActivity().getSupportFragmentManager().setFragmentResultListener(
                DummyDeviceInterfaceDialog.RESULT_INTERFACE,
                this,
                (requestKey, bundle) -> {
                    HashMap<String,String> deviceInterfaceMap = (HashMap<String, String>) bundle.getSerializable(DummyDeviceInterfaceDialog.RESULT_INTERFACE);

                    com.example.networker.ui.viewmodel.DeviceInterface deviceInterface = com.example.networker.ui.viewmodel.DeviceInterface.fromMap(deviceInterfaceMap);

                    addInterface(deviceInterface);
                    dialogFragment.dismiss();
                }
        );
    }

    private void addInterface(com.example.networker.ui.viewmodel.DeviceInterface deviceInterface) {
        if (interfacesList != null) {
            interfacesList.addInterface(deviceInterface);

            Bundle resultBundle = new Bundle();
            resultBundle.putSerializable(RESULT_INTERFACE, deviceInterface.toMap());
            getParentFragmentManager().setFragmentResult(RESULT_INTERFACE, resultBundle);
        }
    }

    private void deleteInterface(int position) {
        if (interfacesList != null) {
            interfacesList.deleteInterface(position);

            Bundle resultBundle = new Bundle();
            resultBundle.putInt(DELETE_INTERFACE, position);
            getParentFragmentManager().setFragmentResult(DELETE_INTERFACE, resultBundle);
        }
    }
}