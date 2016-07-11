package com.artec.mobile.clienti.addAbono;

import com.artec.mobile.clienti.addAbono.events.AddAbonoEvent;
import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.entities.User;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AddAbonoRepositoryImpl implements AddAbonoRepository {
    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;

    public AddAbonoRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
    }

    @Override
    public void addAbono(final Producto producto, final double abono, final Client client) {
        final Firebase ventasReference = firebaseAPI.getProductsReference(client.getEmail())
                .child(producto.getId());
        ventasReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Producto productoUpdated = dataSnapshot.getValue(Producto.class);

                if (productoUpdated != null) {
                    producto.setAbono(producto.getAbono() + abono);
                    ventasReference.setValue(producto);

                    client.setPagado(client.getPagado() + abono);
                    updateClient(client);
                }else{
                    postError();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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

                    postSuccess(client);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                postError();
            }
        });
    }

    private void postSuccess(Client client){
        post(false, client);
    }

    private void postError(){
        post(true, null);
    }

    private void post(boolean error, Client client) {
        AddAbonoEvent event = new AddAbonoEvent();
        event.setError(error);
        event.setClient(client);
        eventBus.post(event);
    }
}
