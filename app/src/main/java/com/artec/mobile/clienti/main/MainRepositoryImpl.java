package com.artec.mobile.clienti.main;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.FirebaseActionListenerCallback;
import com.artec.mobile.clienti.domain.FirebaseEventListenerCallback;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.main.events.MainEvent;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

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
            public void onError(FirebaseError error) {
                if (error != null){
                    post(MainEvent.CONTACT_READ, error.getMessage());
                }else{
                    post(MainEvent.CONTACT_READ, "");
                }
            }
        });

        firebaseAPI.subscribeMain(new FirebaseEventListenerCallback() {
            @Override
            public void onChildAdded(DataSnapshot snapshot) {
                String email = snapshot.getKey();
                email = email.replace("_", ".");

                Client client = new Client();
                client.setEmail(email);
                client = snapshot.getValue(Client.class);

                post(MainEvent.CONTACT_ADDED, client);
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot) {
                String email = snapshot.getKey();
                email = email.replace("_", ".");

                Client client = snapshot.getValue(Client.class);
                client.setEmail(email);
                post(MainEvent.CONTACT_CHANGED, client);
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {}

            @Override
            public void onCancelled(FirebaseError error) {
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

    private void post(int type, Client client){
        post(type, null, client);
    }

    private void post(int type, String error){
        post(type, error, null);
    }

    private void post(int type, String error, Client client) {
        MainEvent event = new MainEvent();
        event.setType(type);
        event.setError(error);
        event.setUser(client);
        eventBus.post(event);
    }
}
