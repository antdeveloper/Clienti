package com.artec.mobile.clienti.entities;

import com.google.firebase.database.Exclude;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
    public double getDeudaTotal() {
        double result = 0.0;
        if (getProductos() != null) {
            //for (int i = 0; i < getProductos().size(); i++) {
                Gson gson = new Gson();
                String json = gson.toJson(getProductos().values());
                Type listType = new TypeToken<List<Producto>>(){}.getType();
                List<Producto> productos = new Gson().fromJson(json, listType);
            for (int i = 0; i < productos.size(); i++) {
                Producto producto = productos.get(i);
                result += producto.getAdeudo();
                //result += ((Producto)getProductos().values().toArray()[i]).getAdeudo();
            }
        }
        return result;
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
}
