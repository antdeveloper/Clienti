package com.artec.mobile.clienti.addAbono;

import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface AddAbonoInteractor {
    void execute(Producto producto, double abono, Client client);
}