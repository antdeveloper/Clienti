package com.artec.mobile.clienti.addAbono.events;

import com.artec.mobile.clienti.entities.Client;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public class AddAbonoEvent {
    boolean error = false;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
