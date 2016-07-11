package com.artec.mobile.clienti.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public class Producto {
    @JsonIgnore
    private String id;
    @JsonIgnore
    private boolean publishByMe;

    private String name;
    private String modelo;
    private double precio;
    private int cantidad;
    private double abono;
    private String url;
    private String email;

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

    public double getAbono() {
        return abono;
    }

    public void setAbono(double abono) {
        this.abono = abono;
    }

    /** Customs get and set **/
    @JsonIgnore
    public double getTotal(){
        return this.precio * this.cantidad;
    }
    @JsonIgnore
    public double getAdeudo(){
        return getTotal() - this.abono;
    }
}
