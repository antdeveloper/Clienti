package com.artec.mobile.clienti.indicadores;

import com.artec.mobile.clienti.indicadores.events.IndicadoresEvent;

/**
 * Created by ANICOLAS on 18/11/2016.
 */

public interface IndicadoresPresenter {
    void onCreate();
    void onDestroy();

    void onGetProducts(String recipient);

    void onEventMainThread(IndicadoresEvent event);
}
