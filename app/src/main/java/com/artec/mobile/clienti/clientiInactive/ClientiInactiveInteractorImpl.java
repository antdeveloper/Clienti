package com.artec.mobile.clienti.clientiInactive;

import com.artec.mobile.clienti.entities.Client;

/**
 * Created by ANICOLAS on 14/11/2016.
 */

public class ClientiInactiveInteractorImpl implements ClientiInactiveInteractor {
    ClientiInactiveRepository repository;

    public ClientiInactiveInteractorImpl(ClientiInactiveRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getClientsInactives() {
        repository.getClientsInactives();
    }

    @Override
    public void reactiveClient(Client client) {
        repository.reactiveClient(client);
    }

    @Override
    public void deleteClient(Client client) {
        repository.deleteClient(client);
    }
}
