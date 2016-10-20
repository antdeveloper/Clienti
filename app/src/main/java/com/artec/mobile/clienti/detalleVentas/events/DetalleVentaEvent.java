package com.artec.mobile.clienti.detalleVentas.events;

import com.artec.mobile.clienti.entities.Event;
import com.artec.mobile.clienti.entities.Producto;

/**
 * Created by ANICOLAS on 18/10/2016.
 */
public class DetalleVentaEvent extends Event{
    private Producto producto;

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
