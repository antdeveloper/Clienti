package com.artec.mobile.clienti.addAbono;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

import java.util.List;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface AddAbonoInteractor {
    void execute(Producto producto, Abono abono, Client client);
    void addAbonoGral(List<Producto> productos, double abonoGral, long fecha, Client client);
}
