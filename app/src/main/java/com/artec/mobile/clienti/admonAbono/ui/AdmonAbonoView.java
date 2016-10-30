package com.artec.mobile.clienti.admonAbono.ui;

import com.artec.mobile.clienti.entities.Abono;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface AdmonAbonoView {
    void showInput();
    void hideInput();
    void showProgress();
    void hideProgress();

    void abonoAdded(Abono abono);
    void abonoUpdated();
    void abonoDeleted();
    void abonoNotAdded();
}
