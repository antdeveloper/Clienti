package com.artec.mobile.clienti.ventas;

import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.ventas.events.VentasEvent;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public interface VentasPresenter {
    void onCreate();
    void onDestroy();

    void subscribe(String recipient);
    void unsubscribe();

    void removePhoto(Producto producto, Client client);
    void onEventMainThread(VentasEvent event);
}
