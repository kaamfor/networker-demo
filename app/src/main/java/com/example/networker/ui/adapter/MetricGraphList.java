package com.example.networker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.networker.R;
import com.example.networker.ui.adapter.viewholder.MetricGraphItem;
import com.example.networker.ui.viewmodel.MetricHistory;

import java.util.ArrayList;

// TODO: mit nem adtam hozzá a gyakorlati videóból:
//  - filter
//  - slide animáció
public class MetricGraphList<V extends Number> extends RecyclerView.Adapter<MetricGraphItem<V>> {
    private final ArrayList<MetricHistory<V>> metricList = new ArrayList<>();
    private ArrayList<MetricHistory<V>> filteredMetricList = metricList;
    private final Context context;

    private final RecyclerView metricSelectorList;
    private int graphWindowSize;

    public MetricGraphList(Context context, RecyclerView metricSelectorList, int graphDefaultWindow) {
        this.context = context;
        this.metricSelectorList = metricSelectorList;
        this.graphWindowSize = graphDefaultWindow;

        metricSelectorList.setAdapter(this);
    }

    public Context getContext() {
        return context;
    }

    public int getGraphWindowSize() {
        return graphWindowSize;
    }

    public void setGraphWindowSize(int graphWindowSize) {
        this.graphWindowSize = graphWindowSize;
    }

    public void addMetric(MetricHistory<V> metricHistory) {
        metricList.add(metricHistory);
        this.notifyDataSetChanged();
    }

    protected MetricHistory<V> getHolderItem(int position) {
        return filteredMetricList.get(position);
    }

    @NonNull
    @Override
    public MetricGraphItem<V> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MetricGraphItem<>(LayoutInflater.from(context).inflate(R.layout.listitem_metric_graph, parent, false), graphWindowSize);
    }

    @Override
    public void onBindViewHolder(@NonNull MetricGraphItem<V> holder, int position) {
        holder.bindTo(getHolderItem(position));
    }

    @Override
    public int getItemCount() {
        return filteredMetricList.size();
    }


}
