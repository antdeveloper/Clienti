package com.artec.mobile.clienti.addAbono.ui;

import com.artec.mobile.clienti.entities.Client;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface AddAbonoView {
    void showInput();
    void hideInput();
    void showProgress();
    void hideProgress();

    void abonoAdded();
    void abonoNotAdded();
}
