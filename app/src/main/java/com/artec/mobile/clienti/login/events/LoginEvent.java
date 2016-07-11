package com.artec.mobile.clienti.login.events;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public class LoginEvent {
    public final static int onSignInError = 0;
    public final static int onSignInSuccess = 1;
    public final static int onFailedToRecoverSession = 2;

    private int eventType;
    private String errorMessage;
    private String currentUserEmail;
    private String currentUsername;

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

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }
}
