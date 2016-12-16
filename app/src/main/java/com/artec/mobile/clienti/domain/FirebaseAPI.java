package com.artec.mobile.clienti.domain;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public class FirebaseAPI {
    private DatabaseReference firebase;
    private DatabaseReference productsSubscribeReference;
    private DatabaseReference clientReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private ChildEventListener productsEventListener;
    private ChildEventListener clientsEventListener;
    private Query queryClients;
    private Activity mContext;

    private final static String SEPARATOR = "___";
    private final static String USERS_PATH = "users";
    private final static String VENTAS_PATH = "products";
    private final static String CLIENTS_PATH = "clients";
    private final static String ABONOS_PATH = "abonos";
    //private final static String FIREBASE_URL = "https://clienti.firebaseio.com/";

    /*public FirebaseAPI() {
        this.firebase = FirebaseDatabase.getInstance().getReference();
        this.mAuth = FirebaseAuth.getInstance();
        this.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null){

                }
            }
        };
    }*/

    /*public FirebaseAPI(DatabaseReference firebase) {
        this.firebase = firebase;
        this.mAuth = FirebaseAuth.getInstance();
        this.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null){

                }
            }
        };
        this.mAuth.addAuthStateListener(mAuthListener);
    }*/

    public FirebaseAPI(DatabaseReference firebase, Activity context) {
        this.firebase = firebase;
        this.mContext = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null){

                }
            }
        };
        this.mAuth.addAuthStateListener(mAuthListener);
    }

    // FIXME: 18/11/2016 //Eliminar
    public void getProductsByClient(final FirebaseActionListenerCallbackMain listenerCallback, String recipient){
        DatabaseReference productsReference = getProductsReference(recipient);
        Query query = productsReference.orderByChild("email").equalTo(getAuthEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    listenerCallback.onSuccess((HashMap<String, ArrayList>) dataSnapshot.getValue());
                }else {
                    listenerCallback.onSuccess(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listenerCallback.onError(databaseError.getMessage());
            }
        });
    }

    public void checkForData(final FirebaseActionListenerCallback listenerCallback, String recipient){
        DatabaseReference productsReference = getProductsReference(recipient);
        Query query = productsReference.orderByChild("email").equalTo(getAuthEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    listenerCallback.onSuccess();
                }else {
                    listenerCallback.onError(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listenerCallback.onError(databaseError.getMessage());
            }
        });
    }

    public void subscribe(final FirebaseEventListenerCallbackVentas listenerCallback, String recipient){
        if (productsEventListener == null){
            productsEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listenerCallback.onChildAdded(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    listenerCallback.onChildUpdated(dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    listenerCallback.onChildRemoved(dataSnapshot);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    listenerCallback.onCancelled(firebaseError);
                }
            };
            productsSubscribeReference = getProductsReference(recipient);
            Query query = productsSubscribeReference.orderByChild("email").equalTo(getAuthUserEmail());
            query.addChildEventListener(productsEventListener);
        }
    }

    public void unsubscribe(){
        if (productsEventListener != null){
            productsSubscribeReference.removeEventListener(productsEventListener);
        }
    }

    public String create(){
        return firebase.push().getKey();
    }

    public void remove(Producto producto, FirebaseActionListenerCallback listenerCallback, String recipent){
        getProductsReference(recipent).child(producto.getId()).removeValue();
        listenerCallback.onSuccess();
    }

    public String getAuthEmail(){
        String email = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            email = user.getEmail();
        }
        return email;
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public void login(String email, String password, final FirebaseActionListenerCallback listenerCallback){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mContext , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Log.w("Task: ", task.getException());
                            listenerCallback.onError(task.getException().getMessage());
                        }else{
                            if (user != null){
                                listenerCallback.onSuccess();
                            }
                        }
                    }
                });
    }

    public void signup(String email, String password, final FirebaseActionListenerCallback listenerCallback){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mContext , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Log.w("Task: ", task.getException());
                            listenerCallback.onError(task.getException().getMessage());
                        }else {
                            if (user != null){
                                listenerCallback.onSuccess();
                            }
                        }
                    }
                });
    }

    public void checkForSession(FirebaseActionListenerCallback listenerCallback){
        //if (firebase.getAuth() != null){
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            listenerCallback.onSuccess();
        }else {
            listenerCallback.onError(null);
        }
    }


    /**
     * Main
     * */

    public void checkForDataMain(final FirebaseActionListenerCallback listenerCallback){
        DatabaseReference clientReference = getMyClientsReference();
        //firebase.addListenerForSingleValueEvent(new ValueEventListener() {
        clientReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    listenerCallback.onSuccess();
                }else {
                    listenerCallback.onError(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                listenerCallback.onError(firebaseError.getMessage());
            }
        });
    }

    public void subscribeMain(final FirebaseEventListenerCallback listenerCallback){
        if (clientsEventListener == null){
            clientsEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listenerCallback.onChildAdded(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    listenerCallback.onChildChanged(dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    //listenerCallback.onChildRemoved(dataSnapshot);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    listenerCallback.onCancelled(firebaseError);
                }
            };

            clientReference = getMyClientsReference();
            //queryClients = clientReference.orderByChild("estatus").startAt(null).endAt(false);
            queryClients = clientReference.orderByChild("estatus").startAt(null).endAt(Constants.ESTATUS_ACTIVO);
        }
        queryClients.addChildEventListener(clientsEventListener);
    }

    public void unsubscribeMain(){
        //if (clientsEventListener != null){
        if (queryClients != null && clientsEventListener != null){
            //clientReference.removeEventListener(clientsEventListener);
            queryClients.removeEventListener(clientsEventListener);
            //clientsEventListener = null;
        }
    }

    public String getAuthUserEmail(){
        String email = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            email = user.getEmail();
        }
        return email;
    }

    public DatabaseReference getUserReference(String email){
        DatabaseReference userReference = null;
        if (email != null){
            String preKey = email.replace("_", "__");
            String emailKey = preKey.replace(".", "_");
            //String emailKey = email.replace(".", "_");
            userReference = firebase.getRoot().child(USERS_PATH).child(emailKey);
        }
        return userReference;
    }

    public DatabaseReference getMyUserReference(){
        return getUserReference(getAuthUserEmail());
    }

    public DatabaseReference getClientsReference(String email){
        return getUserReference(email).child(CLIENTS_PATH);
    }

    public DatabaseReference getMyClientsReference(){
        return getClientsReference(getAuthUserEmail());
    }

    public DatabaseReference getProductsReference(String receiver){
        String preKey = getAuthUserEmail().replace("_", "__");
        String keySender = preKey.replace(".", "_");
        //String keySender = getAuthUserEmail().replace(".", "_");
        String preKeyReceiver = receiver.replace("_", "__");
        String keyReceiver = preKeyReceiver.replace(".", "_");
        //String keyReceiver = receiver.replace(".", "_");

        String keyProducts = keySender + SEPARATOR + keyReceiver;
        if (keySender.compareTo(keyReceiver) > 0){
            keyProducts = keyReceiver + SEPARATOR + keySender;
        }
        return firebase.getRoot().child(VENTAS_PATH).child(keyProducts);
    }

    public void destroyMainListener(){
        clientsEventListener = null;
    }

    /**
     * ClientiInactive
     * */

    public void checkForClientiInactive(final FirebaseActionListenerCallback listenerCallback){
        DatabaseReference clientReference = getMyClientsReference();
        clientReference.keepSynced(true);
        Query queryClients = clientReference.orderByChild("estatus").startAt(Constants.ESTATUS_INACTIVO).endAt(Constants.ESTATUS_INACTIVO);
        queryClients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    listenerCallback.onSuccess();
                }else {
                    listenerCallback.onError(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                listenerCallback.onError(firebaseError.getMessage());
            }
        });
    }

    public void getMyClientsInactives(final FirebaseEventListenerCalbackClientiInactive listenerCallback){
        DatabaseReference reference = getMyClientsReference();
        reference.keepSynced(true);
        Query queryClients = reference.orderByChild("estatus").startAt(Constants.ESTATUS_INACTIVO).endAt(Constants.ESTATUS_INACTIVO);
        queryClients.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listenerCallback.onGetChilds(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listenerCallback.onCancelled(databaseError);
            }
        });
    }

    /****
     * Indicadores
     * ***/

    public void checkForProductos(final FirebaseActionListenerCallback listenerCallback, String recipient){
        DatabaseReference productsReference = getProductsReference(recipient);
        Query query = productsReference.orderByChild("email").equalTo(getAuthEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    listenerCallback.onSuccess();
                }else {
                    listenerCallback.onError(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listenerCallback.onError(databaseError.getMessage());
            }
        });
    }

    public void getProductsByClient(final FirebaseEventListenerCalbackClientiInactive listenerCallback,
                                      String recipient){
        DatabaseReference productsReference = getProductsReference(recipient);
        productsReference.keepSynced(true);
        Query query = productsReference.orderByChild("email").equalTo(getAuthEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listenerCallback.onGetChilds(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listenerCallback.onCancelled(databaseError);
            }
        });
    }

    // ABONOS
    public DatabaseReference getAbonosReference(DatabaseReference reference){
        return reference.child(ABONOS_PATH).push();
    }

    public DatabaseReference getAbonoUpdateReference(DatabaseReference reference, String abonoId){
        return reference.child(ABONOS_PATH).child(abonoId);
    }

    public void deleteAbono(Producto producto, final Abono abono, Client client,
                            FirebaseActionListenerCallback listenerCallback){
        getProductsReference(client.getEmail()).child(producto.getId()).child(ABONOS_PATH)
                .child(abono.getId()).removeValue();
        listenerCallback.onSuccess();
    }
}
