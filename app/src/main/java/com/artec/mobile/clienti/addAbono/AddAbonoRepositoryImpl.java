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

import java.util.List;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AddAbonoRepositoryImpl implements AddAbonoRepository {
    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;
    private double abonoTotal;
    private List<Producto> productos;
    private int i = 0;

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

    @Override
    public void addAbonoGral(List<Producto> productos, double abonoGral, long fecha, Client client) {
        this.productos = productos;
        this.i = productos.size()-1;
        setAbonoTotal(abonoGral);
        DatabaseReference ventasReference = firebaseAPI.getProductsReference(client.getEmail());
        Abono abono = new Abono();
        abono.setFecha(fecha);
        addAbonoRecursivo(abono, ventasReference);

        /*final DatabaseReference ventasReference = firebaseAPI.getProductsReference(client.getEmail());
        setAbonoTotal(abonoGral);

        for (Producto producto : productos){
            final double abonoTotal = getAbonoTotal();
            if (abonoTotal > 0){
                final Abono abono = new Abono();
                abono.setFecha(fecha);
                abono.setValor(abonoTotal > producto.getAdeudo()? producto.getAdeudo() : abonoTotal);
                final DatabaseReference abonosReference = firebaseAPI.getAbonosReference(ventasReference.child(producto.getId()));
                abonosReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        abonosReference.setValue(abono);
                        setAbonoTotal(abonoTotal-abono.getValor());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        postError();
                    }
                });
            }
        }*/
    }

    private void addAbonoRecursivo(final Abono abono, final DatabaseReference ventasReference) {
        Producto producto = productos.get(i);
        if (producto.getAdeudo() > 0) {
            abono.setValor(getAbonoTotal() > productos.get(i).getAdeudo() ? productos.get(i).getAdeudo() : getAbonoTotal());
            final DatabaseReference abonosReference = firebaseAPI.getAbonosReference(ventasReference.child(producto.getId()));
            abonosReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    abonosReference.setValue(abono);
                    setAbonoTotal(abonoTotal - abono.getValor());
                    if (getAbonoTotal() == 0) {
                        postSuccess();
                    } else {
                        i--;
                        addAbonoRecursivo(abono, ventasReference);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    postError();
                }
            });
        }else {
            i--;
            addAbonoRecursivo(abono, ventasReference);
        }
    }

    public void setAbonoTotal(double abonoTotal) {
        this.abonoTotal = abonoTotal;
    }

    public double getAbonoTotal() {
        return abonoTotal;
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
