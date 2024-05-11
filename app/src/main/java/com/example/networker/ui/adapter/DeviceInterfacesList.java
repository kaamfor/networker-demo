package com.example.networker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.networker.R;
import com.example.networker.database.model.device.DeviceInterface;

import java.util.ArrayList;

public class DeviceInterfacesList extends RecyclerView.Adapter<DeviceInterfacesList.ViewHolder> {
    private final ArrayList<DeviceInterface> interfaceList = new ArrayList<>();
    private ArrayList<DeviceInterface> filteredInterfaceList = interfaceList;
    private final Context context;

    private final RecyclerView interfaceSelectorList;

    public DeviceInterfacesList(Context context, RecyclerView interfaceSelectorList) {
        this.context = context;
        this.interfaceSelectorList = interfaceSelectorList;

        interfaceSelectorList.setAdapter(this);
    }

    public RecyclerView getInterfaceSelectorList() {
        return interfaceSelectorList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ifType;
        private TextView address;
        private TextView netmask;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ifType = itemView.findViewById(R.id.interface_type);
            address = itemView.findViewById(R.id.address);
            netmask = itemView.findViewById(R.id.netmask);
        }

        public void bindTo(DeviceInterface deviceInterface) {
            ifType.setText(deviceInterface.getInterfaceType().toString());
            address.setText(deviceInterface.getAddress());
            netmask.setText(String.valueOf(deviceInterface.getMaskLength()));
        }

        public TextView getIfType() {
            return ifType;
        }

        public TextView getAddress() {
            return address;
        }

        public TextView getNetmask() {
            return netmask;
        }
    }

    public ArrayList<DeviceInterface> getFilteredInterfaceList() {
        return filteredInterfaceList;
    }

    public void addInterface(DeviceInterface device) {
        interfaceList.add(device);
        this.notifyDataSetChanged();
    }
    public void deleteInterface(int position) {
        interfaceList.remove(position);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_interface, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceInterface deviceInterface = filteredInterfaceList.get(position);

        holder.bindTo(deviceInterface);
    }

    @Override
    public int getItemCount() {
        return filteredInterfaceList.size();
    }


}
