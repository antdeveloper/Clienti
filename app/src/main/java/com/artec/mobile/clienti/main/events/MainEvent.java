package com.artec.mobile.clienti.main.events;

import com.artec.mobile.clienti.entities.Client;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public class MainEvent {
    private int type;
    private Client user;
    private HashMap<String, ArrayList> productos;
    private String error;

    public final static int CONTACT_READ = 0;
    public final static int CONTACT_ADDED = 1;
    public final static int CONTACT_CHANGED = 2;
    public final static int GET_PRODUCTOS = 3;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Client getUser() {
        return user;
    }

    public void setUser(Client user) {
        this.user = user;
    }

    public HashMap<String, ArrayList> getProductos() {
        return productos;
    }

    public void setProductos(HashMap<String, ArrayList> productos) {
        this.productos = productos;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
