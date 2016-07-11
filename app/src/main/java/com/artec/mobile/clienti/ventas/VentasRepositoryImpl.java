package com.artec.mobile.clienti.ventas;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.FirebaseActionListenerCallback;
import com.artec.mobile.clienti.domain.FirebaseEventListenerCallbackVentas;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.entities.User;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.ventas.events.VentasEvent;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public class VentasRepositoryImpl implements VentasReposiroty {
    private EventBus eventBus;
    private FirebaseAPI firebase;

    public VentasRepositoryImpl(EventBus eventBus, FirebaseAPI firebase) {
        this.eventBus = eventBus;
        this.firebase = firebase;
    }

    @Override
    public void subscribe(String recipient) {

        firebase.checkForData(new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(FirebaseError error) {
                if (error != null){
                    post(VentasEvent.READ_EVENT, error.getMessage());
                }else {
                    post(VentasEvent.READ_EVENT, "");
                }
            }
        }, recipient);

        firebase.subscribe(new FirebaseEventListenerCallbackVentas() {
            @Override
            public void onChildAdded(DataSnapshot snapshot) {
                Producto producto = snapshot.getValue(Producto.class);
                producto.setId(snapshot.getKey());

                String email = firebase.getAuthEmail();
                boolean publishedByMy = producto.getEmail().equals(email);
                producto.setPublishByMe(publishedByMy);
                post(VentasEvent.READ_EVENT, producto);
            }

            @Override
            public void onChildUpdated(DataSnapshot snapshot) {
                Producto producto = snapshot.getValue(Producto.class);
                producto.setId(snapshot.getKey());

                String email = firebase.getAuthEmail();
                boolean publishedByMy = producto.getEmail().equals(email);
                producto.setPublishByMe(publishedByMy);
                post(VentasEvent.CHANGE_EVENT, producto);
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                Producto producto = snapshot.getValue(Producto.class);
                producto.setId(snapshot.getKey());
                post(VentasEvent.DELETE_EVENT, producto);
            }

            @Override
            public void onCancelled(FirebaseError error) {
                post(VentasEvent.READ_EVENT, error.getMessage());
            }
        }, recipient);
    }

    @Override
    public void unsubscribe() {
        firebase.unsubscribe();
    }

    @Override
    public void removePhoto(final Producto producto, final Client client) {
        firebase.remove(producto, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                updateClient(client, producto);
                post(VentasEvent.DELETE_EVENT, producto);
            }

            @Override
            public void onError(FirebaseError error) {
                post(VentasEvent.DELETE_EVENT, error.getMessage());
            }
        }, client.getEmail());
    }

    private void updateClient(final Client client, final Producto producto){
        final String key = client.getEmail().replace(".", "_");
        Firebase userReference = firebase.getUserReference(client.getEmail());
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    Firebase myContactReference = firebase.getMyClientsReference();
                    client.setPagado(client.getPagado() + producto.getAdeudo());
                    myContactReference.child(key).setValue(client);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                post(VentasEvent.DELETE_EVENT, firebaseError.getMessage());
            }
        });
    }

    private void post(int type, Producto producto){
        post(type, null, producto);
    }

    private void post(int type, String error){
        post(type, error, null);
    }

    private void post(int type, String error, Producto producto) {
        VentasEvent event = new VentasEvent();
        event.setType(type);
        event.setError(error);
        event.setProducto(producto);
        eventBus.post(event);
    }
}
