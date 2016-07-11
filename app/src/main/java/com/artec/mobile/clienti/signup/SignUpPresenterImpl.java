package com.artec.mobile.clienti.signup;

import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.signup.events.SignUpEvent;
import com.artec.mobile.clienti.signup.ui.SignUpView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ANICOLAS on 07/06/2016.
 */
public class SignUpPresenterImpl implements SignUpPresenter {
    private EventBus eventBus;
    private SignUpView signUpView;
    private SignUpInteractor signUpInteractor;

    public SignUpPresenterImpl(EventBus eventBus, SignUpView signUpView, SignUpInteractor signUpInteractor) {
        this.eventBus = eventBus;
        this.signUpView = signUpView;
        this.signUpInteractor = signUpInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDrestoy() {
        signUpView = null;
        eventBus.unregister(this);
    }

    @Override
    public void registerNewUser(String email, String password, String username) {
        if (signUpView != null){
            signUpView.disableInputs();
            signUpView.showProgress();
        }

        signUpInteractor.doSignUp(email, password, username);
    }

    @Override
    @Subscribe
    public void onEventMainThread(SignUpEvent event) {
        switch (event.getEventType()){
            case SignUpEvent.onSignInSuccess:{
                onSignInSuccess(event.getCurrentUserEmail(), event.getCurrentUsername());
                break;
            }
            case SignUpEvent.onSignUpSuccess:{
                onSignUpSuccess();
                break;
            }
            case SignUpEvent.onSignInError:{
                onSignInError(event.getErrorMessage());
            }
            case SignUpEvent.onSignUpError:{
                onSignUpError(event.getErrorMessage());
                break;
            }
            case SignUpEvent.onFailedToRecoverSession:{
                onFailedToRecoverSession();
            }
        }
    }

    private void onFailedToRecoverSession() {
        if (signUpView != null){
            signUpView.hideProgress();
            signUpView.enableInputs();
        }
    }

    private void onSignInSuccess(String email, String username){
        if (signUpView != null){
            signUpView.setUsernameEmail(email, username);
            signUpView.navigateToMainScreen();
        }
    }

    private void onSignUpSuccess(){
        if (signUpView != null){
            signUpView.newUserSuccess();
        }
    }

    private void onSignInError(String error){
        if (signUpView != null){
            signUpView.hideProgress();
            signUpView.enableInputs();
            signUpView.loginError(error);
        }
    }

    private void onSignUpError(String error){
        if (signUpView != null){
            signUpView.hideProgress();
            signUpView.enableInputs();
            signUpView.newUserError(error);
        }
    }
}
