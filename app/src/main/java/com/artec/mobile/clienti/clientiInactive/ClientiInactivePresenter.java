package com.artec.mobile.clienti.clientiInactive;

import com.artec.mobile.clienti.clientiInactive.events.ClientiInactiveEvent;
import com.artec.mobile.clienti.entities.Client;

/**
 * Created by ANICOLAS on 14/11/2016.
 */

public interface ClientiInactivePresenter {
    void onCreate();
    void onDestroy();

    void onGetClients();
    void onReactiveClient(Client client);
    void onDeleteClient(Client client);

    void onEventMainThread(ClientiInactiveEvent evt);
}
