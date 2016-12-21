package com.artec.mobile.clienti.login;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.FirebaseActionListenerCallback;
import com.artec.mobile.clienti.entities.User;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.login.events.LoginEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ANICOLAS on 07/06/2016.
 */
public class LoginRepositoryImpl implements LoginRepository{
    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;
    private DatabaseReference myUserReference;

    public LoginRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
    }

    @Override
    public void signIn(String email, String password) {
        if (email != null && password != null){
            firebaseAPI.login(email, password, new FirebaseActionListenerCallback() {
                @Override
                public void onSuccess() {
                    String email = firebaseAPI.getAuthEmail();
                    initSignIn(email);
                }

                @Override
                public void onError(String error) {
                    postEvent(LoginEvent.onSignInError, error);
                }
            });
        }else{
            firebaseAPI.checkForSession(new FirebaseActionListenerCallback() {
                @Override
                public void onSuccess() {
                    String email = firebaseAPI.getAuthEmail();
                    initSignIn(email);
                }

                @Override
                public void onError(String error) {
                    postEvent(LoginEvent.onFailedToRecoverSession);
                }
            });
        }
    }

    private void initSignIn(final String email){
        myUserReference = firebaseAPI.getMyUserReference();
        myUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                if (currentUser != null){
                    postEvent(LoginEvent.onSignInSuccess, email, currentUser.getUsername());
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    private void postEvent(int type, String errorMessage,
                           String currentUserEmail, String username){
        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setEventType(type);
        loginEvent.setCurrentUserEmail(currentUserEmail);
        loginEvent.setCurrentUsername(username);
        if (errorMessage != null){
            loginEvent.setErrorMessage(errorMessage);
        }
        eventBus.post(loginEvent);
    }

    private void postEvent(int type){
        postEvent(type, null, null, null);
    }

    private void postEvent(int type, String error){
        postEvent(type, error, null, null);
    }

    private void postEvent(int type, String currentUserEmail, String username){
        postEvent(type, null, currentUserEmail, username);
    }
}
