package com.artec.mobile.clienti.productos.ui;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public interface ProductosView {
    void showProgress();
    void hideProgress();

    void enableUIElements();
    void disableUIElements();

    void onUploadInit();
    void onUploadComplete();
    void onUploadError(String error);
    void onAbonoAdded(Abono abono);
}
