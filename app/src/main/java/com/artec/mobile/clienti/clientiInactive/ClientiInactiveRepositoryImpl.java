package com.artec.mobile.clienti.clientiInactive;

import com.artec.mobile.clienti.clientiInactive.events.ClientiInactiveEvent;
import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.FirebaseActionListenerCallback;
import com.artec.mobile.clienti.domain.FirebaseEventListenerCalbackClientiInactive;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ANICOLAS on 14/11/2016.
 */

public class ClientiInactiveRepositoryImpl implements ClientiInactiveRepository {
    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;

    public ClientiInactiveRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
    }

    @Override
    public void getClientsInactives() {
        firebaseAPI.checkForClientiInactive(new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                firebaseAPI.getMyClientsInactives(new FirebaseEventListenerCalbackClientiInactive() {
                    @Override
                    public void onGetChilds(DataSnapshot snapshot) {
                        List<Client> clients = new ArrayList<Client>();
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            Client client = postSnapshot.getValue(Client.class);
                            clients.add(client);
                        }
                        post(ClientiInactiveEvent.GET_CLIENTS, clients);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        post(ClientiInactiveEvent.ERROR, error.getMessage());
                    }
                });
            }

            @Override
            public void onError(String error) {
                if (error == null){
                    post(ClientiInactiveEvent.READ, "");
                }else{
                    post(ClientiInactiveEvent.ERROR, error);
                }
            }
        });
        /*firebaseAPI.getMyClientsInactives(new FirebaseEventListenerCalbackClientiInactive() {
            @Override
            public void onGetChilds(DataSnapshot snapshot) {
                List<Client> clients = new ArrayList<Client>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Client university = postSnapshot.getValue(Client.class);
                    clients.add(university);
                }
                post(ClientiInactiveEvent.GET_CLIENTS, clients);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                post(ClientiInactiveEvent.ERROR, error.getMessage());
            }
        });*/
    }

    @Override
    public void reactiveClient(Client client) {
        String preKey = client.getEmail().replace("_", "__");
        String key = preKey.replace(".", "_");

        DatabaseReference myContactReference = firebaseAPI.getMyClientsReference().child(key);
        myContactReference.setValue(client);

        post(ClientiInactiveEvent.REACTIVED_CLIENT, client);
    }

    @Override
    public void deleteClient(Client client) {
        // FIXME: 16/11/2016
    }

    private void post(int type, Client client){
        post(type, null, client, null);
    }

    private void post(int type, String error){
        post(type, error, null, null);
    }

    private void post(int type, List<Client> clients){
        post(type, null, null, clients);
    }

    private void post(int type, String error, Client client, List<Client> clients) {
        ClientiInactiveEvent event = new ClientiInactiveEvent();
        event.setType(type);
        event.setError(error);
        event.setClient(client);
        event.setClients(clients);
        eventBus.post(event);
    }
}
