package com.example.networker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.networker.ui.viewmodel.DummyDevice;


public class DummyDeviceDetailsFragment extends Fragment {
    private static final String LOG_TAG = DummyDeviceDetailsFragment.class.getName();
    public static final String RESULT_DEVICE = DummyDeviceDetailsFragment.class.getName() + "_device";
    private DummyDevice device;
    private EditText nameInput;
    private EditText addressInput;
    private Button saveBtn;

    public DummyDeviceDetailsFragment() {
        super();
        device = null;
    }

    public DummyDeviceDetailsFragment(DummyDevice device) {
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
        return inflater.inflate(R.layout.fragment_dummydevice_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameInput = view.findViewById(R.id.name);
        addressInput = view.findViewById(R.id.address);
        saveBtn = view.findViewById(R.id.save_btn);

        saveBtn.setOnClickListener(l -> saveDevice());

        bindToDevice();
    }

    public void bindToDevice() {
        if (device != null) {
            if (nameInput != null) {
                nameInput.setText(device.getName());
            }
            if (addressInput != null) {
                addressInput.setText(device.getLocationUri());
            }
        }
    }

    public void saveDevice() {
        if (device != null) {
            device.setName(nameInput.getText().toString());

            Bundle resultBundle = new Bundle();
            resultBundle.putSerializable(RESULT_DEVICE, device.toMap());
            getParentFragmentManager().setFragmentResult(RESULT_DEVICE, resultBundle);
        }
    }
}