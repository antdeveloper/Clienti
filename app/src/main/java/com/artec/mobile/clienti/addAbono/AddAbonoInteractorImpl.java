package com.artec.mobile.clienti.addAbono;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AddAbonoInteractorImpl implements AddAbonoInteractor {
    AddAbonoRepository repository;

    public AddAbonoInteractorImpl(AddAbonoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Producto producto, Abono abono, Client client) {
        repository.addAbono(producto, abono, client);
    }
}
