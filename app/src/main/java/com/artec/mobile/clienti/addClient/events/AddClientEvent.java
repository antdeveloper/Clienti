package com.artec.mobile.clienti.addClient.events;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public class AddClientEvent {
    boolean error = false;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
