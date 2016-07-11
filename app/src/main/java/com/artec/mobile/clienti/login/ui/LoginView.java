package com.artec.mobile.clienti.login.ui;

/**
 * Created by ANICOLAS on 07/06/2016.
 */
public interface LoginView {
    void enableInputs();
    void disableInputs();
    void showProgress();
    void hideProgress();

    void handleSignIn();

    void navigateToMainScreen();
    void loginError(String error);

    void setUsernameEmail(String email, String username);
}
