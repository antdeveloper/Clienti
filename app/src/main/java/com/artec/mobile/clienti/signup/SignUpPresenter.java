package com.artec.mobile.clienti.signup;

import com.artec.mobile.clienti.signup.events.SignUpEvent;

/**
 * Created by ANICOLAS on 07/06/2016.
 */
public interface SignUpPresenter {
    void onCreate();
    void onDrestoy();

    void registerNewUser(String email, String password, String username);
    void onEventMainThread(SignUpEvent event);
}
