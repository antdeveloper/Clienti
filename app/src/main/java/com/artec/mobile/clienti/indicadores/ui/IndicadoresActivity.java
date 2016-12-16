package com.artec.mobile.clienti.indicadores.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.IndicadorAbonoSemana;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.indicadores.IndicadoresPresenter;
import com.artec.mobile.clienti.indicadores.events.IndicadoresEvent;
import com.artec.mobile.clienti.indicadores.utils.DayAxisValueFormatter;
import com.artec.mobile.clienti.indicadores.utils.MyAxisValueFormatter;
import com.artec.mobile.clienti.indicadores.utils.XYMarkerView;
import com.artec.mobile.clienti.main.ui.adapters.MainAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ANICOLAS on 17/11/2016.
 */

public class IndicadoresActivity extends AppCompatActivity implements IndicadoresView,
        OnChartValueSelectedListener {

    @Bind(R.id.bchAbonos)
    BarChart bchAbonos;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.container)
    RelativeLayout container;

    @Inject
    IndicadoresPresenter presenter;

    private ClientiApp app;

    protected RectF mOnValueSelectedRectF = new RectF();

    private ArrayList<Client> clients;
    private ArrayList<Producto> productos;
    private int indexClient = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicadores);
        ButterKnife.bind(this);

        app = (ClientiApp)getApplication();
        setupInjection();
        //setupChart();

        this.clients = new ArrayList<>();
        this.productos = new ArrayList<>();
        presenter.onCreate();

        if (MainAdapter.getEmails().size() > 0) {
            getData();
        }
    }

    private void setupInjection() {
        app.getIndicadoresComponent(this, this).inject(this);
    }

    private void setupChart(){
        bchAbonos.setOnChartValueSelectedListener(this);
        bchAbonos.setDrawBarShadow(false);
        bchAbonos.setDrawValueAboveBar(true);
        bchAbonos.getDescription().setEnabled(false);
        //Max values will be 60, if more that these, no will draw
        bchAbonos.setMaxVisibleValueCount(60);
        bchAbonos.setPinchZoom(false);
        bchAbonos.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(bchAbonos);

        XAxis xAxis = bchAbonos.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(2f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = bchAbonos.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = bchAbonos.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = bchAbonos.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(bchAbonos); // For bounds control
        bchAbonos.setMarker(mv); // Set the marker to the chart

        setData();
    }

    private void getData() {
        presenter.onGetProducts(MainAdapter.getEmails().get(indexClient));
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onGetProducts(List<Producto> productos) {
        indexClient++;
        if (productos == null){
            if (indexClient < MainAdapter.getEmails().size()) {
                getData();
            } else {
                indexClient = 0;
                setupChart();
            }
            return;
        }

        Client client = new Client();
        client.setProductoList(productos);
        clients.add(client);
        this.productos.addAll(productos);
        if (indexClient < MainAdapter.getEmails().size()) {
            getData();
        } else {
            indexClient = 0;
            setupChart();
        }
    }

    @Override
    public void onError(int type, String error) {
        switch (type){
            case IndicadoresEvent.ERROR:
                showSnackbar(error);
                break;
        }
    }

    // FIXME: 19/11/2016
    public static class WeekComparator implements Comparator<Abono> {

        /*@Override
        public int compare(Date o1, Date o2) {
            int result = getWeekOfYear(o1) - getWeekOfYear(o2);
            if (result == 0) {
                result = o1.compareTo(o2);
            }
            return result;
        }*/

        @Override
        public int compare(Abono lhs, Abono rhs) {
            int result = getWeekOfYear(lhs.getFechaDate()) - getWeekOfYear(rhs.getFechaDate());
            if (result == 0) {
                result = lhs.getFechaDate().compareTo(rhs.getFechaDate());
            }
            return result;
        }
    }
    // FIXME: 19/11/2016
    protected static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    private void setData() {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        Calendar calendar = Calendar.getInstance();
        int weeksOfYear = calendar.getActualMaximum(Calendar.WEEK_OF_YEAR);
        ArrayList<IndicadorAbonoSemana> abonoSemanas = new ArrayList<>();
        for (int i=1; i<=weeksOfYear; i++){
            abonoSemanas.add(new IndicadorAbonoSemana());
        }
        ArrayList<Abono> abonos = new ArrayList<>();
        for (Client client : clients){
            if (client.getProductoList() != null){
                for (Producto producto : client.getProductoList()){
                    if (producto.getAbonos() != null){
                        for (int i = 0; i < producto.getAbonos().values().size(); i++) {
                            abonos.add((Abono) producto.getAbonos().values().toArray()[i]);
                        }
                    }
                }
            }
        }
        int woy = -1;
        Collections.sort(abonos, new WeekComparator());
        for (Abono abono : abonos) {
            if (woy != getWeekOfYear(abono.getFechaDate())) {
                woy = getWeekOfYear(abono.getFechaDate());
                abonoSemanas.get(woy).addAbono(abono);
                abonoSemanas.get(woy).setNumeroSemana(woy);
            }else {
                abonoSemanas.get(woy).addAbono(abono);
            }
        }


        for (int i = getWeekOfYear(calendar.getTime())-8; i <= getWeekOfYear(calendar.getTime()); i++){
            int index = i;
            if (i < 0){
                index = weeksOfYear-i;
            }
            yVals1.add(new BarEntry(i, (float)abonoSemanas.get(index).getCantidad()));
        }

        BarDataSet set1;

        if (bchAbonos.getData() != null &&
                bchAbonos.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) bchAbonos.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            bchAbonos.getData().notifyDataChanged();
            bchAbonos.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Ingresos en las últimas 10 semanas");
            set1.setColors(ContextCompat.getColor(this, R.color.colorPrimary));
            //set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            bchAbonos.setData(data);
        }
        bchAbonos.invalidate();
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        bchAbonos.getBarBounds((BarEntry) e, bounds);
        MPPointF position = bchAbonos.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + bchAbonos.getLowestVisibleX() + ", high: "
                        + bchAbonos.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {
    }

    private void showSnackbar(String msg) {
        showSnackbar(msg, Snackbar.LENGTH_SHORT);
    }

    private void showSnackbar(int strResource) {
        showSnackbar(getString(strResource), Snackbar.LENGTH_SHORT);
    }

    private void showSnackbar(int strResource, int duration) {
        showSnackbar(getString(strResource), duration);
    }

    private void showSnackbar(String msg, int duration) {
        Snackbar.make(container, msg, duration).show();
    }
}
