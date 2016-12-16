package com.artec.mobile.clienti.indicadores;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.FirebaseActionListenerCallback;
import com.artec.mobile.clienti.domain.FirebaseEventListenerCalbackClientiInactive;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.indicadores.events.IndicadoresEvent;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANICOLAS on 18/11/2016.
 */

public class IndicadoresRepositoryImpl implements IndicadoresRepository {
    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;

    public IndicadoresRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
    }

    @Override
    public void getProductos(final String recipient) {
        firebaseAPI.checkForProductos(new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                firebaseAPI.getProductsByClient(new FirebaseEventListenerCalbackClientiInactive() {
                    @Override
                    public void onGetChilds(DataSnapshot snapshot) {
                        List<Producto> productos = new ArrayList<>();
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            Producto producto = postSnapshot.getValue(Producto.class);
                            productos.add(producto);
                        }
                        post(IndicadoresEvent.SUCCESS, productos);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        post(IndicadoresEvent.ERROR, error.getMessage());
                    }
                }, recipient);
            }

            @Override
            public void onError(String error) {
                if (error == null){
                    post(IndicadoresEvent.SUCCESS);
                }else{
                    post(IndicadoresEvent.ERROR, error);
                }
            }
        }, recipient);
    }

    private void post(int type){
        post(type, null, null);
    }

    private void post(int type, String error){
        post(type, error, null);
    }

    private void post(int type, List<Producto> productos){
        post(type, null, productos);
    }

    private void post(int type, String error, List<Producto> productos) {
        IndicadoresEvent event = new IndicadoresEvent();
        event.setType(type);
        event.setError(error);
        event.setProductos(productos);
        eventBus.post(event);
    }
}
