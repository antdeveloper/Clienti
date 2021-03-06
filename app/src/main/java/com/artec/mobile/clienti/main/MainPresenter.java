package com.artec.mobile.clienti.main;

import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.main.events.MainEvent;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface MainPresenter {
    void onCreate();
    void onDestroy();
    void onPause();
    void onResume();

    void onGetAdeudo(String recipient);
    void onUpdateClient(Client client);

    void logout();
    void onEventMainThread(MainEvent event);
}
