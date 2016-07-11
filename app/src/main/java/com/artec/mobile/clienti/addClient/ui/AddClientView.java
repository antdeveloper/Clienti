package com.artec.mobile.clienti.addClient.ui;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface AddClientView {
    void showInput();
    void hideInput();
    void showProgress();
    void hideProgress();

    void contactAdded();
    void contactNotAdded();
}
