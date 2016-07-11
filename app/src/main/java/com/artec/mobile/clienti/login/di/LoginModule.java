package com.artec.mobile.clienti.login.di;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.login.LoginInteractor;
import com.artec.mobile.clienti.login.LoginInteractorImpl;
import com.artec.mobile.clienti.login.LoginPresenter;
import com.artec.mobile.clienti.login.LoginPresenterImpl;
import com.artec.mobile.clienti.login.LoginRepository;
import com.artec.mobile.clienti.login.LoginRepositoryImpl;
import com.artec.mobile.clienti.login.ui.LoginView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
@Module
public class LoginModule {
    LoginView view;

    public LoginModule(LoginView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    LoginView privideLoginView(){
        return this.view;
    }

    @Provides
    @Singleton
    LoginPresenter privideLoginPresenter(EventBus eventBus, LoginView loginView, LoginInteractor loginInteractor){
        return new LoginPresenterImpl(eventBus, loginView, loginInteractor);
    }

    @Provides
    @Singleton
    LoginInteractor privideLoginInteractor(LoginRepository loginRepository){
        return new LoginInteractorImpl(loginRepository);
    }

    @Provides
    @Singleton
    LoginRepository privideLoginRepository(EventBus eventBus, FirebaseAPI firebaseAPI){
        return new LoginRepositoryImpl(eventBus, firebaseAPI);
    }
}
