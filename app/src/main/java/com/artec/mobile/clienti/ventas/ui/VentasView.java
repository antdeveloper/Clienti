package com.artec.mobile.clienti.ventas.ui;

import com.artec.mobile.clienti.entities.Producto;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public interface VentasView {
    void showList();
    void hideList();
    void showProgress();
    void hideProgress();

    void addProducto(Producto producto);
    void removeProducto(Producto producto);
    void updateProducto(Producto producto);
    void onProductoError(String error);
}
