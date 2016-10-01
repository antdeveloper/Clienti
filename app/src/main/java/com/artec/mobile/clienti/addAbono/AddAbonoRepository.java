package com.artec.mobile.clienti.addAbono;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface AddAbonoRepository {
    void addAbono(Producto producto, Abono abono, Client client);
}
