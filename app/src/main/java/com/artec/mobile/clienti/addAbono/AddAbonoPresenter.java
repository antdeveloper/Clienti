package com.artec.mobile.clienti.addAbono;

import com.artec.mobile.clienti.addAbono.events.AddAbonoEvent;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface AddAbonoPresenter {
    void onShow();
    void onDestroy();

    void addAbono(Producto producto, double abono, Client client);
    void onEventMainThread(AddAbonoEvent event);
}
