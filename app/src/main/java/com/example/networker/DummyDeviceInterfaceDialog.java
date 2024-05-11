package com.example.networker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.networker.ui.viewmodel.DeviceInterface;

import java.util.function.Function;

public class DummyDeviceInterfaceDialog extends DialogFragment {
    private static final String LOG_TAG = DummyDeviceInterfaceDialog.class.getName();
    public static final String RESULT_INTERFACE = DummyDeviceInterfaceDialog.class.getName() + "_interface";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_dummydevice_interface_creator, container, false);

        Spinner typeInput = layout.findViewById(R.id.type);
        EditText ipAddressInput = layout.findViewById(R.id.ip_address);
        EditText netmaskInput = layout.findViewById(R.id.netmask);
        Button submitBtn = layout.findViewById(R.id.login_btn);

        DeviceInterface.Type[] types = {
                DeviceInterface.Type.WAN,
                DeviceInterface.Type.LAN
        };

        typeInput.setAdapter(
                new ArrayAdapter<DeviceInterface.Type>(
                        getActivity(),
                        android.R.layout.simple_spinner_item,
                        types
                )
        );

        DeviceInterface deviceInterface = new DeviceInterface();
        Function<String,Boolean> isAddressValid = (address) -> Patterns.IP_ADDRESS.matcher(address).matches();

        ipAddressInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                Log.i(LOG_TAG, String.valueOf(isAddressValid.apply(s.toString())));

                if (isAddressValid.apply(s.toString())) {
                    ipAddressInput.setTextColor(0xFF00E000);
                }
                else {
                    ipAddressInput.setTextColor(0xFFFF0000);
                }
            }
        });

        submitBtn.setOnClickListener(l -> {
            if (isAddressValid.apply(ipAddressInput.getText().toString())) {
                deviceInterface.setInterfaceType(types[typeInput.getSelectedItemPosition()]);
                deviceInterface.setAddress(ipAddressInput.getText().toString());
                deviceInterface.setMaskLength(Integer.parseInt(netmaskInput.getText().toString()));

                Bundle resultBundle = new Bundle();
                resultBundle.putSerializable(RESULT_INTERFACE, deviceInterface.toMap());
                getParentFragmentManager().setFragmentResult(RESULT_INTERFACE, resultBundle);
            }
        });

        return layout;
    }
}
