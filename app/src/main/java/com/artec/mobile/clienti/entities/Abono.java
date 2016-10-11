package com.artec.mobile.clienti.entities;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ANICOLAS on 06/08/2016.
 */
public class Abono {
    @Exclude
    String id;
    double valor;
    long fecha;

    public Abono() {

    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    @Exclude
    public Date getFechaDate(){
        return new Date(this.fecha);
    }
    @Exclude
    public String getDateFormatted(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        return simpleDateFormat.format(fecha);
    }
    @Exclude
    public String getId() {
        return id;
    }
    @Exclude
    public void setId(String id) {
        this.id = id;
    }
}
