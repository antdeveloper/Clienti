package com.artec.mobile.clienti.main;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.FirebaseActionListenerCallback;
import com.artec.mobile.clienti.domain.FirebaseActionListenerCallbackMain;
import com.artec.mobile.clienti.domain.FirebaseEventListenerCallback;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.main.events.MainEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ANICOLAS on 01/07/2016.
 */
public class MainRepositoryImpl implements MainRepository{
    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;

    public MainRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
    }

    @Override
    public void signOff() {
        firebaseAPI.logout();
    }

    @Override
    public void subscribeToContactListEvents() {
        firebaseAPI.checkForDataMain(new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {}

            @Override
            public void onError(String error) {
                if (error != null){
                    post(MainEvent.CONTACT_READ, error);
                }else{
                    post(MainEvent.CONTACT_READ, "");
                }
            }
        });

        firebaseAPI.subscribeMain(new FirebaseEventListenerCallback() {
            @Override
            public void onChildAdded(DataSnapshot snapshot) {
                String email = snapshot.getKey();
                String preEmail = email.replace("_", ".");
                email = preEmail.replace("..", "_");
                //email = email.replace("_", ".");

                Client client = new Client();
                client.setEmail(email);
                client = snapshot.getValue(Client.class);

                post(MainEvent.CONTACT_ADDED, client);
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot) {
                String email = snapshot.getKey();
                String preEmail = email.replace("_", ".");
                email = preEmail.replace("..", "_");
                //email = email.replace("_", ".");

                Client client = snapshot.getValue(Client.class);
                client.setEmail(email);
                post(MainEvent.CONTACT_CHANGED, client);
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {}

            @Override
            public void onCancelled(DatabaseError error) {
                post(MainEvent.CONTACT_READ, error.getMessage());
            }
        });
    }

    @Override
    public void unsubscribeToContactListEvents() {
        firebaseAPI.unsubscribeMain();
    }

    @Override
    public void destroyListener() {
        firebaseAPI.destroyMainListener();
    }

    @Override
    public void getAdeudo(String recipient) {
        firebaseAPI.getProductsByClient(new FirebaseActionListenerCallbackMain() {
            @Override
            public void onSuccess(HashMap<String, ArrayList> productos) {
                post(MainEvent.GET_PRODUCTOS, productos);
            }

            @Override
            public void onError(String error) {
                post(MainEvent.CONTACT_READ, error);
            }
        }, recipient);
    }

    private void post(int type, Client client){
        post(type, null, client, null);
    }

    private void post(int type, String error){
        post(type, error, null, null);
    }

    private void post(int type, HashMap<String, ArrayList> productos){
        post(type, null, null, productos);
    }

    private void post(int type, String error, Client client, HashMap<String, ArrayList> productos) {
        MainEvent event = new MainEvent();
        event.setType(type);
        event.setError(error);
        event.setUser(client);
        event.setProductos(productos);
        eventBus.post(event);
    }
}
