package com.artec.mobile.clienti.clientiInactive;

import com.artec.mobile.clienti.entities.Client;

/**
 * Created by ANICOLAS on 14/11/2016.
 */

public interface ClientiInactiveRepository {
    void getClientsInactives();
    void reactiveClient(Client client);
    void deleteClient(Client client);
}
