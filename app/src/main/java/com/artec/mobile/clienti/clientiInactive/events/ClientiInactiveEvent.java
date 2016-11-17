package com.artec.mobile.clienti.clientiInactive.events;

import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Event;

import java.util.List;

/**
 * Created by ANICOLAS on 14/11/2016.
 */
public class ClientiInactiveEvent extends Event{
    public static final int GET_CLIENTS = 1;
    public static final int REACTIVED_CLIENT = 2;
    public static final int DELETED_CLIENT = 3;

    private List<Client> clients;
    private Client client;

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
