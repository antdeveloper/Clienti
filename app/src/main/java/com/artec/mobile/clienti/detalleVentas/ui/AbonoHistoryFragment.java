package com.artec.mobile.clienti.detalleVentas.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.detalleVentas.ui.adapters.AbonoAdapter;
import com.artec.mobile.clienti.detalleVentas.ui.adapters.OnAbonoClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbonoHistoryFragment extends Fragment implements AbonoHistoryFragmentListener,
        OnAbonoClickListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private AbonoAdapter adapter;

    public AbonoHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_abono_history, container, false);
        ButterKnife.bind(this, view);

        initAdapter();
        initRecyclerView();
        return view;
    }

    private void initAdapter(){
        if (adapter == null){
            adapter = new AbonoAdapter(getActivity().getApplicationContext(), this);
        }
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void addAbono(Abono abono) {
        adapter.add(abono);
    }

    @Override
    public void updateAbono(Abono abono) {

    }

    @Override
    public void deleteAbono(Abono abono) {

    }

    @Override
    public void OnItemLongClick(Abono abono) {

    }
}
