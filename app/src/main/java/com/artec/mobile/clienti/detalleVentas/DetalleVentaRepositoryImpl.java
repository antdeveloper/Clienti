package com.artec.mobile.clienti.detalleVentas;

import com.artec.mobile.clienti.detalleVentas.events.DetalleVentaEvent;
import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.libs.base.ImageStorage;
import com.artec.mobile.clienti.libs.base.ImageStorageFinishedListener;
import com.google.firebase.database.DatabaseReference;

import java.io.File;

/**
 * Created by ANICOLAS on 18/10/2016.
 */

public class DetalleVentaRepositoryImpl implements DetalleVentaRepository {
    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;
    private ImageStorage imageStorage;

    public DetalleVentaRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI, ImageStorage imageStorage) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
        this.imageStorage = imageStorage;
    }

    @Override
    public void updateProduct(final Producto producto, String path, String clientEmail) {
        final DatabaseReference ventasReference = firebaseAPI.getProductsReference(clientEmail);
        //final String newPhotoId = ventasReference.push().getKey();

        //producto.setId(newPhotoId);
        //producto.setEmail(firebaseAPI.getAuthEmail());
        post(DetalleVentaEvent.INIT);
        ImageStorageFinishedListener listener = new ImageStorageFinishedListener() {
            @Override
            public void onSuccess() {
                addProducto(imageStorage.getImageUrl(firebaseAPI.getAuthEmail()+"/"+producto.getId()),
                        producto.getId(), producto, ventasReference);
            }

            @Override
            public void onError(String error) {
                post(DetalleVentaEvent.ERROR, error);
            }
        };
        if (path.isEmpty()){
            addProducto("", producto.getId(), producto, ventasReference);
        }else {
            imageStorage.upload(new File(path), producto.getId(), firebaseAPI.getAuthEmail(), listener);
        }
    }

    private void addProducto(String imageStorageUrl, String newPhotoId, Producto producto,
                             DatabaseReference ventasReference){
        if (!imageStorageUrl.isEmpty()) {
            producto.setUrl(imageStorageUrl);
        }
        ventasReference.child(newPhotoId).setValue(producto);

        post(DetalleVentaEvent.SUCCESS);
    }

    private void post(int type) {
        post(type, null);
    }

    private void post(int type, String error) {
        DetalleVentaEvent event = new DetalleVentaEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }
}
