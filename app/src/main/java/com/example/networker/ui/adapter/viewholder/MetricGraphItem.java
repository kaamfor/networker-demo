package com.example.networker.ui.adapter.viewholder;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.networker.MainActivity;
import com.example.networker.R;
import com.example.networker.ui.viewmodel.LiveMetricHistory;
import com.example.networker.ui.viewmodel.MetricHistory;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import io.reactivex.rxjava3.disposables.Disposable;

public class MetricGraphItem<V extends Number> extends RecyclerView.ViewHolder {
    private static final String LOG_TAG = MetricGraphItem.class.getName();
    private GraphView graph;
    private LineGraphSeries<DataPoint> graphSeries = new LineGraphSeries<>();
    private int graphInputSize = 0;
    private int windowSize;
    private Disposable metricListener;

    public MetricGraphItem(@NonNull View itemView, int windowSize) {
        super(itemView);

        graph = itemView.findViewById(R.id.graph);
        this.windowSize = windowSize;
        graph.addSeries(graphSeries);
    }

    protected GraphView getGraph() {
        return graph;
    }

    protected LineGraphSeries<DataPoint> getGraphSeries() {
        return graphSeries;
    }

    public int getGraphInputSize() {
        return graphInputSize;
    }

    public int getWindowSize() {
        return windowSize;
    }

    protected void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public void bindTo(MetricHistory<V> metricHistory) {
        if (metricListener != null && !metricListener.isDisposed()) {
            metricListener.dispose();
        }

        graphInputSize = 0;
        DataPoint[] graphData = new DataPoint[metricHistory.size()];
        for (V data : metricHistory.getMetricData()) {
            graphData[graphInputSize] = new DataPoint(graphInputSize, (double)data);
            graphInputSize++;
        }
        graphSeries.resetData(graphData);

        if (metricHistory instanceof LiveMetricHistory) {
            metricListener = ((LiveMetricHistory<V>) metricHistory).listenUpdates().subscribe(this::appendData);
        }

        // TODO: kattintható CardView elem + hozzá handler
    }

    public void appendData(V metric) {
        //Log.i(LOG_TAG, "Metric: " + String.valueOf(windowSize) + " " + String.valueOf(graphInputSize) + " " + String.valueOf(metric));

        graphSeries.appendData(new DataPoint((double) graphInputSize, (double)metric), true, windowSize);
        graphInputSize++;
    }
}
