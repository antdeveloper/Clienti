package com.artec.mobile.clienti.entities;

/**
 * Created by ANICOLAS on 18/10/2016.
 */

public class Event {
    private int type;
    private String error;

    public final static int SUCCESS = 0;
    public final static int ERROR = 100;
    public final static int INIT = 200;

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
}
