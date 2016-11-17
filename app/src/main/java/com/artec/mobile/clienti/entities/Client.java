package com.artec.mobile.clienti.entities;

import com.artec.mobile.clienti.libs.Constants;
import com.google.firebase.database.Exclude;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ANICOLAS on 08/07/2016.
 */
public class Client {
    private String username;
    private String email;
    private boolean isPartner;
    //private boolean down;
    private double estatus;

    @Exclude
    private double adeudo;
    @Exclude
    private double pagado;
    @Exclude
    private double deudaTotal;
    @Exclude
    private HashMap<String, ArrayList> productos;

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

    /*public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }*/

    public double getEstatus() {
        return estatus;
    }

    public void setEstatus(double estatus) {
        this.estatus = estatus;
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
    @Exclude
    public double calcDeudaTotal() {
        double result = 0.0;
        if (getProductos() != null) {
                Gson gson = new Gson();
                String json = gson.toJson(getProductos().values());
                Type listType = new TypeToken<List<Producto>>(){}.getType();
                List<Producto> productos = new Gson().fromJson(json, listType);
            for (int i = 0; i < productos.size(); i++) {
                Producto producto = productos.get(i);
                result += producto.getAdeudo();
            }
        }
        this.deudaTotal = result;
        return result;
    }
    @Exclude
    public double getDeudaTotal() {
        return this.deudaTotal;
    }
    @Exclude
    public void setDeudaTotal(double deudaTotal) {
        this.deudaTotal = deudaTotal;
    }
    @Exclude
    public HashMap<String, ArrayList> getProductos() {
        return productos;
    }
    @Exclude
    public void setProductos(HashMap<String, ArrayList> productos) {
        this.productos = productos;
    }
    @Exclude
    public boolean isLocalUser() {
        return email.contains(Constants.DOMINIO_CLIENTI);
    }
}
