package com.artec.mobile.clienti.productos;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.entities.User;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.libs.base.ImageStorage;
import com.artec.mobile.clienti.libs.base.ImageStorageFinishedListener;
import com.artec.mobile.clienti.productos.events.ProductosEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
    public void uploadPhoto(final Producto producto, final Abono abono, String path, final Client client) {
        final DatabaseReference ventasReference = firebaseAPI.getProductsReference(client.getEmail());
        final String newPhotoId = ventasReference.push().getKey();

        producto.setId(newPhotoId);
        producto.setEmail(firebaseAPI.getAuthEmail());
        post(ProductosEvent.UPLOAD_INIT);
        ImageStorageFinishedListener listener = new ImageStorageFinishedListener() {
            @Override
            public void onSuccess() {
                /*String url = imageStorage.getImageUrl(newPhotoId);
                producto.setUrl(url);
                ventasReference.child(newPhotoId).setValue(producto);

                if (abono.getValor() != 0){
                    addAbono(abono, ventasReference.child(newPhotoId));
                }
                *//*client.setPagado(client.getPagado() + producto.getAbono());
                client.setAdeudo(client.getAdeudo() + producto.getTotal());
                updateClient(client);*//*
                post(ProductosEvent.UPLOAD_COMPLETE);*/
                addProducto(imageStorage.getImageUrl(newPhotoId), newPhotoId, producto, ventasReference, abono);
            }

            @Override
            public void onError(String error) {
                post(ProductosEvent.UPLOAD_ERROR, error);
            }
        };
        if (path.isEmpty()){
            addProducto("", newPhotoId, producto, ventasReference, abono);
        }else {
            imageStorage.upload(new File(path), newPhotoId, listener);
        }
    }

    private void addProducto(String imageStorageUrl, String newPhotoId, Producto producto,
                             DatabaseReference ventasReference, Abono abono){
        producto.setUrl(imageStorageUrl);
        ventasReference.child(newPhotoId).setValue(producto);

        if (abono.getValor() != 0){
            addAbono(abono, ventasReference.child(newPhotoId));
        }
        post(ProductosEvent.UPLOAD_COMPLETE);
    }

    public void addAbono(final Abono abono, final DatabaseReference reference) {
        /*final String key = client.getEmail().replace(".", "_");
        final DatabaseReference userReference = firebaseAPI.getUserReference(client.getEmail());*/

        final DatabaseReference abonosReference = firebaseAPI.getAbonosReference(reference);
        abonosReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                abonosReference.setValue(abono);

                post(ProductosEvent.ABONO_ADDED, abono);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                post(ProductosEvent.UPLOAD_ERROR, databaseError.getMessage());
            }
        });

        /*userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    Client client = new Client();
                    DatabaseReference myContactReference = firebaseAPI.getMyClientsReference();
                    client.setEmail(email);
                    client.setUsername(user.getUsername());
                    client.setPartner(false);
                    myContactReference.child(key).setValue(client);

                    postSuccess();
                }else {
                    postError();
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {}
        });*/
    }

    /*private void updateClient(final Client client){
        final String key = client.getEmail().replace(".", "_");
        DatabaseReference userReference = firebaseAPI.getUserReference(client.getEmail());
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    DatabaseReference myContactReference = firebaseAPI.getMyClientsReference();
                    myContactReference.child(key).setValue(client);

                    post(ProductosEvent.CLIENT_CHANGED, client);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                post(ProductosEvent.UPLOAD_ERROR, firebaseError.getMessage());
            }
        });
    }*/

    private void post(int type, String error){
        post(type, error, null);
    }

    private void post(int type, Abono abono){
        post(type, null, abono);
    }

    private void post(int type) {
        post(type, null, null);
    }

    private void post(int type, String error, Abono abono) {
        ProductosEvent event = new ProductosEvent();
        event.setType(type);
        event.setError(error);
        event.setAbono(abono);
        eventBus.post(event);
    }
}
