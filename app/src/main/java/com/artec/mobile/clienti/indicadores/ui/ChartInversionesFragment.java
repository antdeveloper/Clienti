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

import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.IndicadorIngresoSemana;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.indicadores.utils.ChartFragmentAux;
import com.artec.mobile.clienti.indicadores.utils.DayAxisValueFormatter;
import com.artec.mobile.clienti.indicadores.utils.IndicadoresAux;
import com.artec.mobile.clienti.indicadores.utils.MyAxisValueFormatter;
import com.artec.mobile.clienti.indicadores.utils.XYMarkerView;
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
public class ChartInversionesFragment extends Fragment implements OnChartValueSelectedListener,
        ChartFragmentAux {


    @Bind(R.id.bchInversiones)
    BarChart bchInversiones;

    private boolean hasData = false;

    protected RectF mOnValueSelectedRectF = new RectF();
    private IndicadoresAux aux;

    public ChartInversionesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart_inversiones, container, false);
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
        bchInversiones.setOnChartValueSelectedListener(this);
        bchInversiones.setDrawBarShadow(false);
        bchInversiones.setDrawValueAboveBar(true);
        bchInversiones.getDescription().setEnabled(false);
        //Max values will be 60, if more that these, no will draw
        bchInversiones.setMaxVisibleValueCount(60);
        bchInversiones.setPinchZoom(false);
        bchInversiones.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(bchInversiones);

        XAxis xAxis = bchInversiones.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(2f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = bchInversiones.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = bchInversiones.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = bchInversiones.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(getActivity(), xAxisFormatter);
        mv.setChartView(bchInversiones); // For bounds control
        bchInversiones.setMarker(mv); // Set the marker to the chart

        setData();
    }

    @Override
    public void refreshChart() {
        bchInversiones.invalidate();
    }

    public static class WeekComparator implements Comparator<Producto> {// FIXME: 16/12/2016 Crear clase externa
        @Override
        public int compare(Producto lhs, Producto rhs) {
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
        ArrayList<IndicadorIngresoSemana> ingresoSemanas = new ArrayList<>();
        for (int i = 1; i <= weeksOfYear; i++) {
            ingresoSemanas.add(new IndicadorIngresoSemana());
        }
        ArrayList<Producto> productos = new ArrayList<>();
        for (Client client : aux.getClients()) {
            if (client.getProductoList() != null) {
                for (Producto producto : client.getProductoList()) {
                    productos.add(producto);
                    /*if (producto.getAbonos() != null) {
                        for (int i = 0; i < producto.getAbonos().values().size(); i++) {
                            productos.add((Producto) producto.getAbonos().values().toArray()[i]);
                        }
                    }*/
                }
            }
        }
        int woy = -1;
        Collections.sort(productos, new WeekComparator());
        for (Producto producto : productos) {
            if (woy != getWeekOfYear(producto.getFechaDate())) {
                woy = getWeekOfYear(producto.getFechaDate());
                ingresoSemanas.get(woy).addProducto(producto);
                ingresoSemanas.get(woy).setNumeroSemana(woy);
            } else {
                ingresoSemanas.get(woy).addProducto(producto);
            }
        }


        for (int i = getWeekOfYear(calendar.getTime()) - 9; i <= getWeekOfYear(calendar.getTime()); i++) {
            int index = i;
            if (i < 0) {
                index = weeksOfYear - i;
            }
            yVals1.add(new BarEntry(i, (float) ingresoSemanas.get(index).getCantidad()));
        }

        BarDataSet set1;

        if (bchInversiones.getData() != null &&
                bchInversiones.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) bchInversiones.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            bchInversiones.getData().notifyDataChanged();
            bchInversiones.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Inversiones en las últimas 10 semanas");
            set1.setColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            bchInversiones.setData(data);
        }
        bchInversiones.invalidate();
        hasData = true;
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        bchInversiones.getBarBounds((BarEntry) e, bounds);
        MPPointF position = bchInversiones.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + bchInversiones.getLowestVisibleX() + ", high: "
                        + bchInversiones.getHighestVisibleX());

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
