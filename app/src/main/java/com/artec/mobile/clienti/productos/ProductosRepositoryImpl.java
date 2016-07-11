package com.artec.mobile.clienti.productos;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.entities.User;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.libs.base.ImageStorage;
import com.artec.mobile.clienti.libs.base.ImageStorageFinishedListener;
import com.artec.mobile.clienti.productos.events.ProductosEvent;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public class ProductosRepositoryImpl implements ProductosRepository {
    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;
    private ImageStorage imageStorage;

    public ProductosRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI, ImageStorage imageStorage) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
        this.imageStorage = imageStorage;
    }

    @Override
    public void uploadPhoto(final Producto producto, String path, final Client client) {
        final Firebase ventasReference = firebaseAPI.getProductsReference(client.getEmail());
        final String newPhotoId = ventasReference.push().getKey();

        producto.setId(newPhotoId);
        producto.setEmail(firebaseAPI.getAuthEmail());
        post(ProductosEvent.UPLOAD_INIT);
        ImageStorageFinishedListener listener = new ImageStorageFinishedListener() {
            @Override
            public void onSuccess() {
                String url = imageStorage.getImageUrl(newPhotoId);
                producto.setUrl(url);
                ventasReference.child(newPhotoId).setValue(producto);

                client.setPagado(client.getPagado() + producto.getAbono());
                client.setAdeudo(client.getAdeudo() + producto.getTotal());
                updateClient(client);

                post(ProductosEvent.UPLOAD_COMPLETE);
            }

            @Override
            public void onError(String error) {
                post(ProductosEvent.UPLOAD_ERROR, error);
            }
        };
        imageStorage.upload(new File(path), newPhotoId, listener);
    }

    private void updateClient(final Client client){
        final String key = client.getEmail().replace(".", "_");
        Firebase userReference = firebaseAPI.getUserReference(client.getEmail());
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    Firebase myContactReference = firebaseAPI.getMyClientsReference();
                    myContactReference.child(key).setValue(client);

                    post(ProductosEvent.CLIENT_CHANGED, client);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                post(ProductosEvent.UPLOAD_ERROR, firebaseError.getMessage());
            }
        });
    }

    private void post(int type, String error){
        post(type, error, null);
    }

    private void post(int type, Client client){
        post(type, null, client);
    }

    private void post(int type) {
        post(type, null, null);
    }

    private void post(int type, String error, Client client) {
        ProductosEvent event = new ProductosEvent();
        event.setType(type);
        event.setError(error);
        event.setClient(client);
        eventBus.post(event);
    }
}
