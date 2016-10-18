package com.artec.mobile.clienti.main.ui;

import com.artec.mobile.clienti.entities.Client;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface MainView {
    void showProgress();
    void hideProgress();

    void onClientAdded(Client client);
    void onClientChanged(Client client);
    void onGetProducts(HashMap<String, ArrayList> productos);
}
