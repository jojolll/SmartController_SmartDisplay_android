package org.koxx.smartlcd;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.koxx.smartlcd.graph.Base;
import org.koxx.smartlcd.graph.MyAxisValueFormatter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Example of a dual axis {@link LineChart} with multiple data sets.
 *
 * @version 3.1.0
 * @since 1.7.4
 */
public class GraphActivity extends Base implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private LineChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    ArrayList<Entry> values1 = new ArrayList<>();
    ArrayList<Entry> values2 = new ArrayList<>();

    int i_speed = 0;
    boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        */
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Speed / current graph");

        /*
        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);

        seekBarX = findViewById(R.id.seekBar1);
        seekBarX.setOnSeekBarChangeListener(this);

        seekBarY = findViewById(R.id.seekBar2);
        seekBarY.setOnSeekBarChangeListener(this);
*/
        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);


        chart.setNoDataText("Scooter needs to be moving to capture datas.");

        // add data
/*
        seekBarX.setProgress(20);
        seekBarY.setProgress(30);
*/

        chart.animateX(1000);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(LegendForm.LINE);
        //l.setTypeface(tfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.BLUE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = chart.getXAxis();
        //xAxis.setTypeface(tfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLUE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value / 10) + "s";
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        // leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
//        leftAxis.setAxisMaximum(50f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        //leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = chart.getAxisRight();
        //rightAxis.setTypeface(tfLight);
        rightAxis.setTextColor(Color.RED);
        //rightAxis.setAxisMaximum(10);
        rightAxis.setAxisMinimum(0);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        //rightAxis.setGranularityEnabled(false);

        BluetoothHandler.getInstance(this).setGraph(this);
        BluetoothHandler.getInstance(this).sendFastUpdateValue((byte) 0x01);

    }

    @Override
    public void onBackPressed() {
        BluetoothHandler.getInstance(this).setGraph(null);
        BluetoothHandler.getInstance(this).sendFastUpdateValue((byte) 0x00);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText(String.valueOf(seekBarX.getProgress()));
        tvY.setText(String.valueOf(seekBarY.getProgress()));

//        setData(seekBarX.getProgress(), seekBarY.getProgress());

        // redraw
        chart.invalidate();
    }

    @Override
    protected void saveToGallery() {
        saveToGallery(chart, "LineChartActivity2");
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());

        chart.centerViewToAnimated(e.getX(), e.getY(), chart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 100);
        //chart.zoomAndCenterAnimated(2.5f, 2.5f, e.getX(), e.getY(), chart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
        //chart.zoomAndCenterAnimated(1.8f, 1.8f, e.getX(), e.getY(), chart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }


    public void addSpeedData(float val) {

        if ((val > -1) || (started)) {
            if (!started) {
                values1.add(new Entry(i_speed, val));
                i_speed++;
            }
            values1.add(new Entry(i_speed, val));
            i_speed++;
            started = true;

            LineDataSet set1 = null, set2 = null;

            if (chart.getData() != null &&
                    chart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
                set1.setValues(values1);

                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();

                chart.setVisibleXRange(0, 200);
                chart.moveViewToX(chart.getData().getEntryCount());

            } else {
                initChart(set1, set2);
            }

        }
    }

    public void addCurrentData(float val) {

        if (started) {
            values2.add(new Entry(i_speed, val));

            if (chart.getData() != null &&
                    chart.getData().getDataSetCount() > 0) {
                LineDataSet set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
                set2.setValues(values2);
            }

        }
    }

    public void initChart(LineDataSet set1, LineDataSet set2) {

        // create a dataset and give it a type
        set1 = new LineDataSet(values1, "Speed (Km/h)");

        set1.setAxisDependency(AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.BLUE);
        set1.setLineWidth(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);

        //set1.setFillFormatter(new MyFillFormatter(0f));
        //set1.setDrawHorizontalHighlightIndicator(false);
        //set1.setVisible(false);
        //set1.setCircleHoleColor(Color.WHITE);

        // create a dataset and give it a type
        set2 = new LineDataSet(values2, "Current (A)");
        set2.setAxisDependency(AxisDependency.RIGHT);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.WHITE);
        set2.setLineWidth(3f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        set2.setDrawCircleHole(false);
        set2.setDrawCircles(false);
        set2.setDrawValues(false);
        //set2.setFillFormatter(new MyFillFormatter(900f));

        // create a data object with the data sets
        LineData data = new LineData(set1, set2);

        data.setValueTextColor(Color.BLUE);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);
    }

}