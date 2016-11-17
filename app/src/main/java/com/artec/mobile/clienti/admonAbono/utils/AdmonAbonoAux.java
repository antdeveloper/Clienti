package com.artec.mobile.clienti.admonAbono.utils;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

import java.util.List;

/**
 * Created by ANICOLAS on 29/10/2016.
 */

public interface AdmonAbonoAux {
    /*void setMode(int mode);*/
    int getMode();
    void abonoAdded(Abono abono);
    /*void abonoUpdated(Abono abono);
    void abonoDeleted(Abono abono);*/
    /*Abono getAbono();*/
    /*void setProducto(Producto producto);*/
    Producto getProducto();
    /*void setProductos(List<Producto> productos);
    List<Producto> getProductos();*/
    Client getClient();
}
