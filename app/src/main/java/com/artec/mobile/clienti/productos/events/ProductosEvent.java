package com.artec.mobile.clienti.productos.events;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public class ProductosEvent {
    private int type;
    private String error;
    private Abono abono;
    public final static int UPLOAD_INIT = 0;
    public final static int UPLOAD_COMPLETE = 1;
    public final static int UPLOAD_ERROR = 2;
    public final static int ABONO_ADDED = 3;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Abono getAbono() {
        return abono;
    }

    public void setAbono(Abono abono) {
        this.abono = abono;
    }
}
