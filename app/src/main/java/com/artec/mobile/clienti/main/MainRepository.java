package com.artec.mobile.clienti.main;

import com.artec.mobile.clienti.entities.Client;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface MainRepository {
    void signOff();
    void subscribeToContactListEvents();
    void unsubscribeToContactListEvents();
    void destroyListener();

    void getAdeudo(String recipient);
    void updateClient(Client client);
}
