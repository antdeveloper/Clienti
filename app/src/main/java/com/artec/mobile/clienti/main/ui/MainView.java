package com.artec.mobile.clienti.main.ui;

import com.artec.mobile.clienti.entities.Client;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface MainView {
    void onClientAdded(Client client);
    void onClientChanged(Client client);
}
