package com.artec.mobile.clienti.domain;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ANICOLAS on 07/06/2016.
 */
public class FirebaseHelper {
    private Firebase firebase;
    private ChildEventListener contactsEventListener;

    private final static String SEPARATOR = "___";
    private final static String USERS_PATH = "users";
    private final static String VENTAS_PATH = "ventas";
    private final static String CONTACTS_PATH = "contacts";

    public FirebaseHelper(Firebase firebase) {
        this.firebase = firebase;
    }

    public void checkForData(final FirebaseActionListenerCallback listenerCallback){
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

    public void subscribe(final FirebaseEventListenerCallback listenerCallback){
        if (contactsEventListener == null){
            contactsEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listenerCallback.onChildAdded(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

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
            firebase.addChildEventListener(contactsEventListener);
        }
    }

    public void unsubscribe(){
        if (contactsEventListener != null){
            firebase.removeEventListener(contactsEventListener);
        }
    }

    //create() update()

    public void remove(String email, FirebaseActionListenerCallback listenerCallback){
        String currentUserEmail = getAuthUserEmail();
        getOneContactReference(currentUserEmail, email).removeValue();
        getOneContactReference(email, currentUserEmail).removeValue();
        listenerCallback.onSuccess();
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

    public void logout(){
        firebase.unauth();
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

    public Firebase getContactsReference(String email){
        return getUserReference(email).child(CONTACTS_PATH);
    }

    public Firebase getMyContancsReference(){
        return getContactsReference(getAuthUserEmail());
    }

    public Firebase getOneContactReference(String mainEmail, String childEmail){
        String childKey = childEmail.replace(".", "_");
        return getUserReference(mainEmail).child(CONTACTS_PATH).child(childKey);
    }

    public Firebase getChatsReference(String receiver){
        String keySender = getAuthUserEmail().replace(".", "_");
        String keyReceiver = receiver.replace(".", "_");

        String keyChat = keySender + SEPARATOR + keyReceiver;
        if (keySender.compareTo(keyReceiver) > 0){
            keyChat = keyReceiver + SEPARATOR + keySender;
        }
        return firebase.getRoot().child(VENTAS_PATH).child(keyChat);
    }

    //Extra
    public void destroyListener(){
        contactsEventListener = null;
    }
}
