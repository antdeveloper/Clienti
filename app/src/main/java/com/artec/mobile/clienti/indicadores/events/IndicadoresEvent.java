package com.artec.mobile.clienti.indicadores.events;

import com.artec.mobile.clienti.entities.Event;
import com.artec.mobile.clienti.entities.Producto;

import java.util.List;

/**
 * Created by ANICOLAS on 18/11/2016.
 */
public class IndicadoresEvent extends Event{
    private List<Producto> productos;

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
