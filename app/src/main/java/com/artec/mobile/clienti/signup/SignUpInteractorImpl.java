package com.artec.mobile.clienti.signup;

/**
 * Created by ANICOLAS on 07/06/2016.
 */
public class SignUpInteractorImpl implements SignUpInteractor {
    private SignUpRepository signUpRepository;

    public SignUpInteractorImpl(SignUpRepository signUpRepository) {
        this.signUpRepository = signUpRepository;
    }

    @Override
    public void doSignUp(String email, String password, String username) {
        signUpRepository.signUp(email, password, username);
    }
}
