package com.artec.mobile.clienti.productos;

import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public interface ProductosRepository {
    void uploadPhoto(Producto producto, String path, Client client);
}
