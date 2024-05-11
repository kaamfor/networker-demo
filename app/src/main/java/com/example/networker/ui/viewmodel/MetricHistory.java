package com.example.networker.ui.viewmodel;

import java.util.LinkedList;
import java.util.List;

// TODO: queue/buffer?
//public class MetricHistory<K,V> {
public class MetricHistory<V> {
    private String name;
    private LinkedList<V> metricData = new LinkedList<>();
    //private @Nullable Iterable<K> dataSamplingIndices;

    public MetricHistory(String name) {
        this.name = name;
    }

    //public MetricHistory(String title, Iterable<V> metricData, Iterable<K> dataSamplingIndices) {
    public MetricHistory(String name, Iterable<V> metricData) {
        if (name == null) {
            throw new IllegalArgumentException("title cannot be null");
        }

        this.name = name;
        if (metricData != null) {
            metricData.forEach(this.metricData::add);
        }
        //this.dataSamplingIndices = dataSamplingIndices;
    }

    public String getName() {
        return name;
    }

    public Iterable<V> getMetricData() {
        return metricData;
    }

    public int size() {
        return metricData.size();
    }

    //public @Nullable Iterable<K> getDataSamplingIndices() {
    //    return dataSamplingIndices;
    //}

    //public void appendData(Iterable<V> metricData, Iterable<K> dataSamplingIndices) {
    public void appendData(Iterable<V> metricData) {
        if (metricData == null) {
            throw new IllegalArgumentException("metricData cannot be null");
        }

        metricData.forEach(this.metricData::add);
        //this.dataSamplingIndices = dataSamplingIndices;
    }

    public void appendData(V data) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot be null");
        }

        this.metricData.add(data);
        //this.dataSamplingIndices = dataSamplingIndices;
    }
}
