package com.artec.mobile.clienti.domain;

import com.artec.mobile.clienti.entities.Producto;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public class FirebaseAPI {
    private Firebase firebase;
    private ChildEventListener productsEventListener;
    private ChildEventListener clientsEventListener;

    private final static String SEPARATOR = "___";
    private final static String USERS_PATH = "users";
    private final static String VENTAS_PATH = "products";
    private final static String CONTACTS_PATH = "clients";
    private final static String FIREBASE_URL = "https://clienti.firebaseio.com/";

    public FirebaseAPI() {
        this.firebase = new Firebase(FIREBASE_URL);
    }

    public FirebaseAPI(Firebase firebase) {
        this.firebase = firebase;
    }

    public void checkForData(final FirebaseActionListenerCallback listenerCallback, String recipient){
        getProductsReference(recipient).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    listenerCallback.onSuccess();
                }else {
                    listenerCallback.onError(null);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listenerCallback.onError(firebaseError);
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
                public void onCancelled(FirebaseError firebaseError) {
                    listenerCallback.onCancelled(firebaseError);
                }
            };
            getProductsReference(recipient).addChildEventListener(productsEventListener);
        }
    }

    public void unsubscribe(){
        if (productsEventListener != null){
            firebase.removeEventListener(productsEventListener);
        }
    }

    public String create(){
        return firebase.push().getKey();
    }

    public void remove(Producto producto, FirebaseActionListenerCallback listenerCallback, String recipent){
        //this.firebase.child(producto.getId()).removeValue();
        getProductsReference(recipent).child(producto.getId()).removeValue();
        listenerCallback.onSuccess();
    }

    public String getAuthEmail(){
        String email = null;
        if (firebase.getAuth() != null){
            Map<String, Object> providerData = firebase.getAuth().getProviderData();
            email = providerData.get("email").toString();
        }
        return email;
    }

    public void logout(){
        firebase.unauth();
    }

    public void login(String email, String password, final FirebaseActionListenerCallback listenerCallback){
        firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                listenerCallback.onSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listenerCallback.onError(firebaseError);
            }
        });
    }

    public void signup(String email, String password, final FirebaseActionListenerCallback listenerCallback){
        firebase.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> o) {
                listenerCallback.onSuccess();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                listenerCallback.onError(firebaseError);
            }
        });
    }

    public void checkForSession(FirebaseActionListenerCallback listenerCallback){
        if (firebase.getAuth() != null){
            listenerCallback.onSuccess();
        }else {
            listenerCallback.onError(null);
        }
    }


    /**
     * Main
     * */

    public void checkForDataMain(final FirebaseActionListenerCallback listenerCallback){
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    listenerCallback.onSuccess();
                }else {
                    listenerCallback.onError(null);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listenerCallback.onError(firebaseError);
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
                    listenerCallback.onChildRemoved(dataSnapshot);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    listenerCallback.onCancelled(firebaseError);
                }
            };
            getMyClientsReference().addChildEventListener(clientsEventListener);
        }
    }

    public void unsubscribeMain(){
        if (clientsEventListener != null){
            firebase.removeEventListener(clientsEventListener);
        }
    }

    public String getAuthUserEmail(){
        AuthData authData = firebase.getAuth();
        String email = null;
        if (authData != null){
            Map<String, Object> providerData = authData.getProviderData();
            email = providerData.get("email").toString();
        }

        return email;
    }

    public Firebase getUserReference(String email){
        Firebase userReference = null;
        if (email != null){
            String emailKey = email.replace(".", "_");
            userReference = firebase.getRoot().child(USERS_PATH).child(emailKey);
        }
        return userReference;
    }

    public Firebase getMyUserReference(){
        return getUserReference(getAuthUserEmail());
    }

    public Firebase getClientsReference(String email){
        return getUserReference(email).child(CONTACTS_PATH);
    }

    public Firebase getMyClientsReference(){
        return getClientsReference(getAuthUserEmail());
    }

    public Firebase getOneClientReference(String mainEmail, String childEmail){
        String childKey = childEmail.replace(".", "_");
        return getUserReference(mainEmail).child(CONTACTS_PATH).child(childKey);
    }

    public Firebase getProductsReference(String receiver){
        String keySender = getAuthUserEmail().replace(".", "_");
        String keyReceiver = receiver.replace(".", "_");

        String keyChat = keySender + SEPARATOR + keyReceiver;
        if (keySender.compareTo(keyReceiver) > 0){
            keyChat = keyReceiver + SEPARATOR + keySender;
        }
        return firebase.getRoot().child(VENTAS_PATH).child(keyChat);
    }

    public void destroyMainListener(){
        clientsEventListener = null;
    }
}
