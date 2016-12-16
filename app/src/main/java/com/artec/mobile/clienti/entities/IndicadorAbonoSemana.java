package com.artec.mobile.clienti.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANICOLAS on 19/11/2016.
 */

public class IndicadorAbonoSemana {
    private int numeroSemana;
    private double cantidad;
    private List<Abono> abonos;

    public IndicadorAbonoSemana() {
        abonos = new ArrayList<>();
    }

    public int getNumeroSemana() {
        return numeroSemana;
    }

    public void setNumeroSemana(int numeroSemana) {
        this.numeroSemana = numeroSemana;
    }

    public double getCantidad() {
        //return cantidad;
        double result = 0.0;
        if (getAbonos() != null) {
            for (Abono abono : getAbonos()) {
                result += abono.getValor();
            }
        }
        this.cantidad = result;
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public List<Abono> getAbonos() {
        return abonos;
    }

    public void setAbonos(List<Abono> abonos) {
        this.abonos = abonos;
    }

    public void addAbono(Abono abono){
        this.abonos.add(abono);
    }

    public int getCountAbonos(){
        return this.abonos.size();
    }
}
