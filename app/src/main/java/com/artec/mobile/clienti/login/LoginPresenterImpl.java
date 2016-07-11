package com.artec.mobile.clienti.login;

import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.login.events.LoginEvent;
import com.artec.mobile.clienti.login.ui.LoginView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ANICOLAS on 07/06/2016.
 */
public class LoginPresenterImpl implements LoginPresenter{
    private EventBus eventBus;
    private LoginView loginView;
    private LoginInteractor loginInteractor;

    public LoginPresenterImpl(EventBus eventBus, LoginView loginView, LoginInteractor loginInteractor) {
        this.eventBus = eventBus;
        this.loginView = loginView;
        this.loginInteractor = loginInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDrestoy() {
        loginView = null;
        eventBus.unregister(this);
    }

    @Override
    public void validateLogin(String email, String password) {
        if (loginView != null){
            loginView.disableInputs();
            loginView.showProgress();
        }

        loginInteractor.doSignIn(email, password);
    }

    @Override
    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        switch (event.getEventType()){
            case LoginEvent.onSignInSuccess:{
                onSignInSuccess(event.getCurrentUserEmail(), event.getCurrentUsername());
                break;
            }
            case LoginEvent.onSignInError:{
                onSignInError(event.getErrorMessage());
            }
            case LoginEvent.onFailedToRecoverSession:{
                onFailedToRecoverSession();
            }
        }
    }

    private void onFailedToRecoverSession() {
        if (loginView != null){
            loginView.hideProgress();
            loginView.enableInputs();
        }
    }

    private void onSignInSuccess(String email, String username){
        if (loginView != null){
            loginView.setUsernameEmail(email, username);
            loginView.navigateToMainScreen();
        }
    }

    private void onSignInError(String error){
        if (loginView != null){
            loginView.hideProgress();
            loginView.enableInputs();
            loginView.loginError(error);
        }
    }
}
