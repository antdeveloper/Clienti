package com.artec.mobile.clienti.entities;

import com.google.firebase.database.Exclude;

import java.util.Map;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public class Producto {
    @Exclude
    private String id;
    @Exclude
    private boolean publishByMe;
    @Exclude
    private double abono;

    private String name;
    private String modelo;
    private double precio;
    private int cantidad;
    private String url;
    private String email;
    Map<String, Abono> abonos;
    private long fechaVenta;
    /*private double descuento;
    private String nombreOferta;
    private String emailVendedor;*/

    public Producto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPublishByMe() {
        return publishByMe;
    }

    public void setPublishByMe(boolean publishByMe) {
        this.publishByMe = publishByMe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Map<String, Abono> getAbonos() {
        return abonos;
    }

    public void setAbonos(Map<String, Abono> abonos) {
        this.abonos = abonos;
    }

    public long getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(long fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    /** Customs get and set **/
    @Exclude
    public double getTotal(){
        return this.precio * this.cantidad;
    }
    @Exclude
    public double getAdeudo(){
        return getTotal() - this.abono;
    }
    @Exclude
    public double getAbono() {
        double sum = 0.0;
        if (this.abonos != null) {
            for (int i = 0; i < this.abonos.values().size(); i++) {
                sum += ((Abono) this.abonos.values().toArray()[i]).getValor();
            }
        }
        return sum;
    }
    @Exclude
    public void setAbono(double abono) {
        this.abono = abono;
    }
}
