package com.artec.mobile.clienti.indicadores.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.indicadores.utils.ChartFragmentAux;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartGananciasFragment extends Fragment implements OnChartValueSelectedListener,
        ChartFragmentAux{

    private boolean hasData = false;

    public ChartGananciasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart_ganancias, container, false);
    }

    @Override
    public void setupChart() {

    }

    @Override
    public void refreshChart() {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public boolean hasData() {
        return hasData;
    }
}
