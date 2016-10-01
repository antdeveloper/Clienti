package com.artec.mobile.clienti.productos;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.productos.events.ProductosEvent;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public interface ProductosPresenter {
    void onCreate();
    void onDestroy();

    void uploadPhoto(Producto producto, Abono abono, String path, Client client);
    void onEventMainThread(ProductosEvent event);
}
