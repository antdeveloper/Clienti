package com.artec.mobile.clienti.ventas;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.FirebaseActionListenerCallback;
import com.artec.mobile.clienti.domain.FirebaseEventListenerCallbackVentas;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.entities.User;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.libs.base.ImageStorage;
import com.artec.mobile.clienti.libs.base.ImageStorageFinishedListener;
import com.artec.mobile.clienti.ventas.events.VentasEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public class VentasRepositoryImpl implements VentasReposiroty {
    private EventBus eventBus;
    private FirebaseAPI firebase;
    private ImageStorage imageStorage;

    public VentasRepositoryImpl(EventBus eventBus, FirebaseAPI firebase, ImageStorage imageStorage) {
        this.eventBus = eventBus;
        this.firebase = firebase;
        this.imageStorage = imageStorage;
    }

    @Override
    public void subscribe(String recipient) {

        firebase.checkForData(new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {}

            @Override
            public void onError(String error) {
                if (error != null){
                    post(VentasEvent.READ_EVENT, error);
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
                producto.setPublishByMe(producto.getEmail().equals(email));
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
            public void onCancelled(DatabaseError error) {
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
        ImageStorageFinishedListener listener = new ImageStorageFinishedListener() {
            @Override
            public void onSuccess() {
                deletePhoto(producto, client);
            }

            @Override
            public void onError(String error) {
                post(VentasEvent.DELETE_EVENT, error);
            }
        };
        if (producto.getUrl().isEmpty()){
            deletePhoto(producto, client);
        }else {
            imageStorage.delete(producto.getId(), firebase.getAuthEmail(), listener);
        }
    }

    private void deletePhoto(final Producto producto, Client client){
        firebase.remove(producto, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                post(VentasEvent.DELETE_EVENT, producto);
            }

            @Override
            public void onError(String error) {
                post(VentasEvent.DELETE_EVENT, error);
            }
        }, client.getEmail());
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
