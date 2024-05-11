package com.example.networker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.networker.R;
import com.example.networker.ui.viewmodel.Device;

import java.util.ArrayList;

// TODO: mit nem adtam hozzá a gyakorlati videóból:
//  - filter
//  - slide animáció
public class DeviceChooser extends RecyclerView.Adapter<DeviceChooser.ViewHolder> {
    private final ArrayList<Device> deviceList = new ArrayList<>();
    private ArrayList<Device> filteredDeviceList = deviceList;
    private final Context context;

    private final RecyclerView deviceSelectorList;

    public DeviceChooser(Context context, RecyclerView deviceSelectorList) {
        this.context = context;
        this.deviceSelectorList = deviceSelectorList;

        deviceSelectorList.setAdapter(this);
    }

    public RecyclerView getDeviceSelectorList() {
        return deviceSelectorList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox deviceConnected;
        private TextView deviceName;
        private TextView connectorName;
        private TextView locationUri;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deviceConnected = itemView.findViewById(R.id.deviceConnected);
            deviceName = itemView.findViewById(R.id.deviceName);
            connectorName = itemView.findViewById(R.id.connectorType);
            locationUri = itemView.findViewById(R.id.locationUri);
        }

        public void bindTo(Device device) {
            deviceConnected.setChecked(Boolean.TRUE.equals(device.isConnected()));
            deviceName.setText(device.getName());
            connectorName.setText(device.getVendor());
            locationUri.setText(device.getLocationUri());

            // TODO: kattintható CardView elem + hozzá handler
        }

        public CheckBox getDeviceConnectedInput() {
            return deviceConnected;
        }

        public TextView getDeviceNameInput() {
            return deviceName;
        }

        public TextView getConnectorNameInput() {
            return connectorName;
        }

        public TextView getLocationUriInput() {
            return locationUri;
        }
    }

    public ArrayList<Device> getFilteredDeviceList() {
        return filteredDeviceList;
    }

    public void addDevice(Device device) {
        deviceList.add(device);
        this.notifyDataSetChanged();
    }
    public void addDevice(Iterable<Device> device) {
        device.forEach(deviceList::add);
        this.notifyDataSetChanged();
    }

    public void deleteDevice(Device device) {
        deviceList.remove(device);
        this.notifyDataSetChanged();
    }

    public void clear() {
        deviceList.clear();
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_device, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Device device = filteredDeviceList.get(position);

        holder.bindTo(device);
    }

    @Override
    public int getItemCount() {
        return filteredDeviceList.size();
    }


}
