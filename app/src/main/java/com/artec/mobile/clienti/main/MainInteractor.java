package com.artec.mobile.clienti.main;

import com.artec.mobile.clienti.entities.Client;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface MainInteractor {
    void subscribe();
    void unsubscribe();

    void destroyListener();

    void getAdeudo(String recipient);
    void updateClient(Client client);
}
