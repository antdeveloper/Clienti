package com.artec.mobile.clienti.admonAbono;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

import java.util.List;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AdmonAbonoInteractorImpl implements AdmonAbonoInteractor {
    AdmonAbonoRepository repository;

    public AdmonAbonoInteractorImpl(AdmonAbonoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Producto producto, Abono abono, Client client) {
        repository.addAbono(producto, abono, client);
    }

    @Override
    public void updateAbono(Producto producto, Abono abono, Client client) {
        repository.updateAbono(producto, abono, client);
    }

    @Override
    public void addAbonoGral(List<Producto> productos, double abonoGral, long fecha, Client client) {
        repository.addAbonoGral(productos, abonoGral, fecha, client);
    }

    @Override
    public void deleteAbono(Producto producto, Abono abono, Client client) {
        repository.deleteAbono(producto, abono, client);
    }
}
