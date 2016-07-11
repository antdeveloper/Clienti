package com.artec.mobile.clienti.login;

/**
 * Created by ANICOLAS on 07/06/2016.
 */
public class LoginInteractorImpl implements LoginInteractor{
    private LoginRepository loginRepository;

    public LoginInteractorImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public void doSignIn(String email, String password) {
        loginRepository.signIn(email, password);
    }
}
