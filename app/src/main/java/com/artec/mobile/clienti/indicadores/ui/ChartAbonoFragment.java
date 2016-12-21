package com.artec.mobile.clienti.indicadores.ui;


import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.IndicadorAbonoSemana;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.indicadores.utils.ChartFragmentAux;
import com.artec.mobile.clienti.indicadores.utils.DayAxisValueFormatter;
import com.artec.mobile.clienti.indicadores.utils.IndicadoresAux;
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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartAbonoFragment extends Fragment implements OnChartValueSelectedListener,
        ChartFragmentAux{

    @Bind(R.id.bchAbonos)
    BarChart bchAbonos;

    private boolean hasData = false;

    protected RectF mOnValueSelectedRectF = new RectF();
    private IndicadoresAux aux;

    public ChartAbonoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart_abono, container, false);
        ButterKnife.bind(this, view);

        aux = ((IndicadoresAux)getActivity());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setupChart() {
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

        XYMarkerView mv = new XYMarkerView(getActivity(), xAxisFormatter);
        mv.setChartView(bchAbonos); // For bounds control
        bchAbonos.setMarker(mv); // Set the marker to the chart

        setData();
    }

    @Override
    public void refreshChart() {
        bchAbonos.invalidate();
    }

    public static class WeekComparator implements Comparator<Abono> {
        @Override
        public int compare(Abono lhs, Abono rhs) {
            int result = getWeekOfYear(lhs.getFechaDate()) - getWeekOfYear(rhs.getFechaDate());
            if (result == 0) {
                result = lhs.getFechaDate().compareTo(rhs.getFechaDate());
            }
            return result;
        }
    }

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
        for (int i = 1; i <= weeksOfYear; i++) {
            abonoSemanas.add(new IndicadorAbonoSemana());
        }
        ArrayList<Abono> abonos = new ArrayList<>();
        for (Client client : aux.getClients()) {
            if (client.getProductoList() != null) {
                for (Producto producto : client.getProductoList()) {
                    if (producto.getAbonos() != null) {
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
            } else {
                abonoSemanas.get(woy).addAbono(abono);
            }
        }


        for (int i = getWeekOfYear(calendar.getTime()) - 9; i <= getWeekOfYear(calendar.getTime()); i++) {
            int index = i;
            if (i < 0) {
                index = weeksOfYear - i;
            }
            yVals1.add(new BarEntry(i, (float) abonoSemanas.get(index).getCantidad()));
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
            set1.setColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
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
        hasData = true;
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

    @Override
    public boolean hasData() {
        return hasData;
    }
}
