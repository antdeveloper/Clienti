package com.artec.mobile.clienti.detalleVentas;

import com.artec.mobile.clienti.detalleVentas.events.DetalleVentaEvent;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

/**
 * Created by ANICOLAS on 18/10/2016.
 */

public interface DetalleVentaPresenter {
    void onCreate();
    void onDestroy();

    void update(Producto producto, String path, String clientEmail);
    void onEventMainThread(DetalleVentaEvent event);
}
