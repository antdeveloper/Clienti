package com.artec.mobile.clienti.signup;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.FirebaseActionListenerCallback;
import com.artec.mobile.clienti.entities.User;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.signup.events.SignUpEvent;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by ANICOLAS on 07/06/2016.
 */
public class SignUpRepositoryImpl implements SignUpRepository {
    private EventBus eventBus;
    private FirebaseAPI firebaseAPI;
    private Firebase myUserReference;

    public SignUpRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
    }

    @Override
    public void signUp(final String email, final String password, final String username) {
        firebaseAPI.signup(email, password, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                postEvent(SignUpEvent.onSignUpSuccess);
                signIn(email, password, username);
            }

            @Override
            public void onError(FirebaseError error) {
                postEvent(SignUpEvent.onSignInError, error.getMessage());
            }
        });
    }

    public void signIn(String email, String password, final String username) {
        if (email != null && password != null){
            firebaseAPI.login(email, password, new FirebaseActionListenerCallback() {
                @Override
                public void onSuccess() {
                    String email = firebaseAPI.getAuthEmail();
                    initSignIn(email, username);
                }

                @Override
                public void onError(FirebaseError error) {
                    postEvent(SignUpEvent.onSignInError, error.getMessage());
                }
            });
        }else{
            firebaseAPI.checkForSession(new FirebaseActionListenerCallback() {
                @Override
                public void onSuccess() {
                    String email = firebaseAPI.getAuthEmail();
                    initSignIn(email, username);
                }

                @Override
                public void onError(FirebaseError error) {
                    postEvent(SignUpEvent.onFailedToRecoverSession);
                }
            });
        }
    }

    private void initSignIn(final String email, final String username){
        myUserReference = firebaseAPI.getMyUserReference();
        myUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                if (currentUser == null){
                    registerNewUser(username);
                }
                postEvent(SignUpEvent.onSignInSuccess, email, username);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void registerNewUser(String username){
        String email = firebaseAPI.getAuthUserEmail();
        if (email != null){
            User currentUser = new User();
            currentUser.setEmail(email);
            currentUser.setUsername(username);
            myUserReference.setValue(currentUser);
        }
    }

    private void postEvent(int type, String errorMessage, String currentUserEmail, String username){
        SignUpEvent signUpEvent = new SignUpEvent();
        signUpEvent.setEventType(type);
        signUpEvent.setCurrentUserEmail(currentUserEmail);
        signUpEvent.setCurrentUsername(username);
        if (errorMessage != null){
            signUpEvent.setErrorMessage(errorMessage);
        }

        eventBus.post(signUpEvent);
    }

    private void postEvent(int type){
        postEvent(type, null, null, null);
    }

    private void postEvent(int type, String error){
        postEvent(type, error, null, null);
    }

    private void postEvent(int type, String currentUserEmail, String currentUsername){
        postEvent(type, null, currentUserEmail, currentUsername);
    }
}
