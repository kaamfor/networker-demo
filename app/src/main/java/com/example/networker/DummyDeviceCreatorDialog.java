package com.example.networker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.networker.ui.viewmodel.DummyDevice;

public class DummyDeviceCreatorDialog extends DialogFragment {
    public static final String RESULT_DUMMYDEVICE = DummyDeviceCreatorDialog.class.getName() + "_device";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_dummydevice_creator, container, false);

        EditText nameInput = layout.findViewById(R.id.name);
        EditText locationUriInput = layout.findViewById(R.id.locationUri);
        Button submitBtn = layout.findViewById(R.id.login_btn);

        DummyDevice device = new DummyDevice();

        locationUriInput.setText(device.getLocationUri());

        submitBtn.setOnClickListener(l -> {
            device.setName(nameInput.getText().toString());

            Bundle resultBundle = new Bundle();
            resultBundle.putSerializable(RESULT_DUMMYDEVICE, device.toMap());
            getParentFragmentManager().setFragmentResult(RESULT_DUMMYDEVICE, resultBundle);
        });



        return layout;
    }
}
