package com.artec.mobile.clienti.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANICOLAS on 19/11/2016.
 */

public class IndicadorIngresoSemana {
    private int numeroSemana;
    private double cantidad;
    private List<Producto> productos;

    public IndicadorIngresoSemana() {
        productos = new ArrayList<>();
    }

    public int getNumeroSemana() {
        return numeroSemana;
    }

    public void setNumeroSemana(int numeroSemana) {
        this.numeroSemana = numeroSemana;
    }

    public double getCantidad() {
        double result = 0.0;
        if (getProductos() != null) {
            for (Producto producto : getProductos()) {
                result += producto.getPrecioOriginal();
            }
        }
        this.cantidad = result;
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void addProducto(Producto producto){
        this.productos.add(producto);
    }

    public int getCountProductos(){
        return this.productos.size();
    }
}
