package com.artec.mobile.clienti.admonAbono.events;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Event;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public class AdmonAbonoEvent extends Event{
    public static final int ABONO_UPDATED = 1;
    public static final int ABONO_DELETED = 2;

    private Abono abono;
    boolean error = false;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Abono getAbono() {
        return abono;
    }

    public void setAbono(Abono abono) {
        this.abono = abono;
    }
}
