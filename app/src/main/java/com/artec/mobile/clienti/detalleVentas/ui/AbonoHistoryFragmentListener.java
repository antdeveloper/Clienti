package com.artec.mobile.clienti.detalleVentas.ui;

import com.artec.mobile.clienti.detalleVentas.ui.adapters.OnAbonoClickListener;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Producto;

/**
 * Created by ANICOLAS on 10/10/2016.
 */
public interface AbonoHistoryFragmentListener {
    void setListenerFragment(OnAbonoClickListener listener);
    void addAbono(Abono abono);
    void updateAbono(Abono abono);
    void deleteAbono(Abono abono);
    double getAbonos();
}
