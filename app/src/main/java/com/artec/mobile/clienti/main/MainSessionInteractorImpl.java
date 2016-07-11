package com.artec.mobile.clienti.main;

/**
 * Created by ANICOLAS on 02/07/2016.
 */
public class MainSessionInteractorImpl implements MainSessionInteractor{
    MainRepository repository;

    public MainSessionInteractorImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void signOff() {
        repository.signOff();
    }
}
