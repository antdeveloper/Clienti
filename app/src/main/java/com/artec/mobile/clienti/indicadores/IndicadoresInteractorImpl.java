package com.artec.mobile.clienti.indicadores;

/**
 * Created by ANICOLAS on 18/11/2016.
 */

public class IndicadoresInteractorImpl implements IndicadoresInteractor {
    private IndicadoresRepository repository;

    public IndicadoresInteractorImpl(IndicadoresRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getProducts(String recipient) {
        repository.getProductos(recipient);
    }
}
