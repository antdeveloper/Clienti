package com.artec.mobile.clienti.entities;

import com.google.firebase.database.Exclude;

/**
 * Created by ANICOLAS on 08/07/2016.
 */
public class Client {
    private String username;
    private String email;
    private boolean isPartner;

    @Exclude
    private double adeudo;
    @Exclude
    private double pagado;

    public Client() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPartner() {
        return isPartner;
    }

    public void setPartner(boolean partner) {
        isPartner = partner;
    }

    @Exclude
    public double getAdeudo() {
        return adeudo;
    }
    @Exclude
    public void setAdeudo(double adeudo) {
        this.adeudo = adeudo;
    }
    @Exclude
    public double getPagado() {
        return pagado;
    }
    @Exclude
    public void setPagado(double pagado) {
        this.pagado = pagado;
    }
}
