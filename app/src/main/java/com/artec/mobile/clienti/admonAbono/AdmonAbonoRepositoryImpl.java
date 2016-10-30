package com.artec.mobile.clienti.admonAbono;

import com.artec.mobile.clienti.admonAbono.events.AdmonAbonoEvent;
import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.FirebaseActionListenerCallback;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AdmonAbonoRepositoryImpl implements AdmonAbonoRepository {
    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;
    private double abonoTotal;
    private List<Producto> productos;
    private int i = 0;

    public AdmonAbonoRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
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
                abono.setId(abonosReference.getKey());
                postSuccess(AdmonAbonoEvent.SUCCESS, abono);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                postError(AdmonAbonoEvent.ERROR);
            }
        });
    }

    @Override
    public void updateAbono(Producto producto, final Abono abono, Client client) {
        final DatabaseReference ventasReference = firebaseAPI.getProductsReference(client.getEmail());
        final DatabaseReference abonosReference = firebaseAPI.getAbonoUpdateReference(
                ventasReference.child(producto.getId()), abono.getId());
        abonosReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                abonosReference.setValue(abono);
                postSuccess(AdmonAbonoEvent.ABONO_UPDATED, null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                postError(AdmonAbonoEvent.ERROR);
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
    }

    @Override
    public void deleteAbono(final Producto producto, Abono abono, Client client) {
        firebaseAPI.deleteAbono(producto, abono, client, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                postSuccess(AdmonAbonoEvent.ABONO_DELETED, null);
            }

            @Override
            public void onError(String error) {
                postError(AdmonAbonoEvent.ERROR);
            }
        });
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
                        postSuccess(AdmonAbonoEvent.SUCCESS, null);
                    } else {
                        i--;
                        addAbonoRecursivo(abono, ventasReference);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    postError(AdmonAbonoEvent.ERROR);
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

    private void postSuccess(int type, Abono abono){
        post(false, type, abono);
    }

    private void postError(int type){
        post(true, type, null);
    }

    private void post(boolean error, int type, Abono abono) {
        AdmonAbonoEvent event = new AdmonAbonoEvent();
        event.setError(error);
        event.setType(type);
        event.setAbono(abono);
        eventBus.post(event);
    }
}
