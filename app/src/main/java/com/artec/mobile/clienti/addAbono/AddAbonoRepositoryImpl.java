package com.artec.mobile.clienti.addAbono;

import com.artec.mobile.clienti.addAbono.events.AddAbonoEvent;
import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.entities.User;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
    public void addAbono(final Producto producto, final Abono abono, final Client client) {
        final DatabaseReference ventasReference = firebaseAPI.getProductsReference(client.getEmail());
        final DatabaseReference abonosReference = firebaseAPI.getAbonosReference(ventasReference.child(producto.getId()));
        abonosReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                abonosReference.setValue(abono);
                postSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                postError();
            }
        });
    }

    private void postSuccess(){
        post(false);
    }

    private void postError(){
        post(true);
    }

    private void post(boolean error) {
        AddAbonoEvent event = new AddAbonoEvent();
        event.setError(error);
        eventBus.post(event);
    }
}
