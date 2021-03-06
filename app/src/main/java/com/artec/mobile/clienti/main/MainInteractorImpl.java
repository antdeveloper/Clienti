package com.artec.mobile.clienti.main;

import com.artec.mobile.clienti.entities.Client;

/**
 * Created by ANICOLAS on 01/07/2016.
 */
public class MainInteractorImpl implements MainInteractor{
    MainRepository repository;

    public MainInteractorImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void subscribe() {
        repository.subscribeToContactListEvents();
    }

    @Override
    public void unsubscribe() {
        repository.unsubscribeToContactListEvents();
    }

    @Override
    public void destroyListener() {
        repository.destroyListener();
    }

    @Override
    public void getAdeudo(String recipient) {
        repository.getAdeudo(recipient);
    }

    @Override
    public void updateClient(Client client) {
        repository.updateClient(client);
    }
}
