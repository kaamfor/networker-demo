package com.example.networker.ui.viewmodel;

import java.util.LinkedList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class LiveMetricHistory<V> extends MetricHistory<V> {
    private final PublishSubject<V> observable = PublishSubject.create();
    public LiveMetricHistory(String name) {
        super(name);
    }

    public LiveMetricHistory(String name, Iterable<V> metricData) {
        super(name, metricData);
    }

    public Observable<V> listenUpdates() {
        return observable;
    }

    @Override
    public void appendData(Iterable<V> metricData) {
        LinkedList<V> dataList = new LinkedList<>();
        for (V metric : metricData) {
            if (metric != null && !observable.hasComplete() && !observable.hasThrowable()) {
                observable.onNext(metric);
            }
            dataList.add(metric);
        }

        super.appendData(dataList);
    }

    @Override
    public void appendData(V data) {
        if (data != null && !observable.hasComplete() && !observable.hasThrowable()) {
            observable.onNext(data);
        }
        super.appendData(data);
    }
}
