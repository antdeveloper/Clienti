package com.artec.mobile.clienti.clientiInactive.ui;

import com.artec.mobile.clienti.entities.Client;

import java.util.List;

/**
 * Created by ANICOLAS on 14/11/2016.
 */

public interface ClientiInactiveView {
    void showProgress();
    void hideProgress();

    void onGetClients(List<Client> clients);
    void onClientDeleted(String email);

    void onError(int type, String error);
}
