package com.artec.mobile.clienti.signup.events;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public class SignUpEvent {
    public final static int onSignInError = 0;
    public final static int onSignUpError = 1;
    public final static int onSignInSuccess = 2;
    public final static int onSignUpSuccess = 3;
    public final static int onFailedToRecoverSession = 4;

    private int eventType;
    private String errorMessage;
    private String currentUserEmail;
    private String currentUsername;

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public int getEventType(){
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }
}
