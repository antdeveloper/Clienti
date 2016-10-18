package com.artec.mobile.clienti.main;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface MainRepository {
    void signOff();
    void subscribeToContactListEvents();
    void unsubscribeToContactListEvents();
    void destroyListener();

    void getAdeudo(String recipient);
}
