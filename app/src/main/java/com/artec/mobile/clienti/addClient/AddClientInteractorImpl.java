package com.artec.mobile.clienti.addClient;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AddClientInteractorImpl implements AddClientInteractor {
    AddClientRepository repository;

    public AddClientInteractorImpl(AddClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String email, String username) {
        repository.addClient(email, username);
    }
}
