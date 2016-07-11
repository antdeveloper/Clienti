package com.artec.mobile.clienti.entities;

/**
 * Created by ANICOLAS on 08/07/2016.
 */
public class Client {
    private String username;
    private String email;
    private double adeudo;
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

    public double getAdeudo() {
        return adeudo;
    }

    public void setAdeudo(double adeudo) {
        this.adeudo = adeudo;
    }

    public double getPagado() {
        return pagado;
    }

    public void setPagado(double pagado) {
        this.pagado = pagado;
    }
}
