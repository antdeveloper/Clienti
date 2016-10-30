package com.artec.mobile.clienti.admonAbono;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

import java.util.List;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface AdmonAbonoRepository {
    void addAbono(Producto producto, Abono abono, Client client);
    void updateAbono(Producto producto, Abono abono, Client client);
    void addAbonoGral(List<Producto> productos, double abonoGral, long fecha, Client client);
    void deleteAbono(Producto producto, Abono abono, Client client);
}
