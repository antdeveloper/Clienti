package com.artec.mobile.clienti.admonAbono.utils;

import com.artec.mobile.clienti.entities.Producto;

import java.util.List;

/**
 * Created by ANICOLAS on 06/11/2016.
 */

public interface AdmonAbonoGralAux {
    void setProductos(List<Producto> productos);
    List<Producto> getProductos();
}
