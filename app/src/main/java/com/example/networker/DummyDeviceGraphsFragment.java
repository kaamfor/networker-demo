package com.example.networker;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.networker.ui.adapter.MetricGraphList;
import com.example.networker.ui.viewmodel.LiveMetricHistory;
import com.example.networker.ui.viewmodel.DummyDevice;


public class DummyDeviceGraphsFragment extends Fragment {
    private static final String LOG_TAG = DummyDeviceGraphsFragment.class.getName();
    private DummyDevice device;
    private RecyclerView graphListView;
    private final int graphListViewGridNumber = 1;
    private MetricGraphList<Double> metricList;
    private Handler handler;

    public DummyDeviceGraphsFragment() {
        super();
        device = null;
    }

    public DummyDeviceGraphsFragment(DummyDevice device) {
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
        return inflater.inflate(R.layout.fragment_dummydevice_graphs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        graphListView = getView().findViewById(R.id.graphList);

        graphListView.setLayoutManager(new GridLayoutManager(view.getContext(), graphListViewGridNumber));
        metricList = new MetricGraphList<>(view.getContext(), graphListView, 40);

        addGraph();
    }

    public void addGraph() {
        LiveMetricHistory<Double> metricHistory = new LiveMetricHistory<>("");
        metricList.addMetric(metricHistory);
        metricHistory.listenUpdates().subscribe(item -> {
            metricList.notifyDataSetChanged();
            //Log.i(LOG_TAG, item.toString());
        });

        Handler currentHandler = new Handler(Looper.getMainLooper());
        Runnable addNewMetric = new Runnable() {
            @Override
            public void run() {
                if (handler != currentHandler) {
                    return;
                }
                metricHistory.appendData(Math.exp(Math.random()+2));
                handler.postAtTime(this, System.currentTimeMillis()+1000);
                handler.postDelayed(this, 1000);
            }
        };
        handler = currentHandler;
        addNewMetric.run();
    }
}