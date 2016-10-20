package com.artec.mobile.clienti.detalleVentas.ui;

import com.artec.mobile.clienti.entities.Abono;

/**
 * Created by ANICOLAS on 10/10/2016.
 */

public interface DetalleVentaView {
    void showProgress();
    void hideProgress();

    void enableUIElements();
    void disableUIElements();

    void productInit();
    void productUpdate();

    void addAbono(Abono abono);
    void removeAbono(Abono abono);
    void updateAbono(Abono abono);
    void onError(String error);
}
