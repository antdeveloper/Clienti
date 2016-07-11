package com.artec.mobile.clienti.productos.ui;

import com.artec.mobile.clienti.entities.Client;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public interface ProductosView {
    void onUploadInit();
    void onUploadComplete();
    void onUploadError(String error);
    void onClientChanged(Client client);
}
