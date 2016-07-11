package com.artec.mobile.clienti.compras.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artec.mobile.clienti.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComprasFragment extends Fragment {


    public ComprasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_productos, container, false);

        return view;
    }

}
