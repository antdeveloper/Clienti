package com.artec.mobile.clienti.login;

import com.artec.mobile.clienti.login.events.LoginEvent;

/**
 * Created by ANICOLAS on 07/06/2016.
 */
public interface LoginPresenter {
    void onCreate();
    void onDrestoy();
    void validateLogin(String email, String password);
    void onEventMainThread(LoginEvent event);
}
