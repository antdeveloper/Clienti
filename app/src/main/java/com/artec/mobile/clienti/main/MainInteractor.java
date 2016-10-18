package com.artec.mobile.clienti.main;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface MainInteractor {
    void subscribe();
    void unsubscribe();
    void destroyListener();

    void getAdeudo(String recipient);
}
