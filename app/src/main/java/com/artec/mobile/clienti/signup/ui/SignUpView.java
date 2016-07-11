package com.artec.mobile.clienti.signup.ui;

/**
 * Created by ANICOLAS on 07/06/2016.
 */
public interface SignUpView {
    void enableInputs();
    void disableInputs();
    void showProgress();
    void hideProgress();

    void handleSignUp();

    void navigateToMainScreen();
    void loginError(String error);

    void newUserSuccess();
    void newUserError(String error);

    void setUsernameEmail(String email, String username);
}
