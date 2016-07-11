package com.artec.mobile.clienti.ventas;

import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public interface VentasReposiroty {
    void subscribe(String recipient);
    void unsubscribe();
    void removePhoto(Producto producto, Client client);
}
