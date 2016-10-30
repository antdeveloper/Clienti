package com.artec.mobile.clienti.admonAbono.utils;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

import java.util.List;

/**
 * Created by ANICOLAS on 29/10/2016.
 */

public interface AdmonAbonoAux {
    int getMode();
    void abonoAdded(Abono abono);
    void abonoUpdated(Abono abono);
    void abonoDeleted(Abono abono);
    Abono getAbono();
    Producto getProducto();
    List<Producto> getProductos();
    Client getClient();
}
