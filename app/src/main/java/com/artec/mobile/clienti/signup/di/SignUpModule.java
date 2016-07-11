package com.artec.mobile.clienti.signup.di;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.signup.SignUpInteractor;
import com.artec.mobile.clienti.signup.SignUpInteractorImpl;
import com.artec.mobile.clienti.signup.SignUpPresenter;
import com.artec.mobile.clienti.signup.SignUpPresenterImpl;
import com.artec.mobile.clienti.signup.SignUpRepository;
import com.artec.mobile.clienti.signup.SignUpRepositoryImpl;
import com.artec.mobile.clienti.signup.ui.SignUpView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
@Module
public class SignUpModule {
    SignUpView view;

    public SignUpModule(SignUpView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    SignUpView privideLoginView(){
        return this.view;
    }

    @Provides
    @Singleton
    SignUpPresenter privideLoginPresenter(EventBus eventBus, SignUpView signUpView, SignUpInteractor signUpInteractor){
        return new SignUpPresenterImpl(eventBus, signUpView, signUpInteractor);
    }

    @Provides
    @Singleton
    SignUpInteractor privideLoginInteractor(SignUpRepository signUpRepository){
        return new SignUpInteractorImpl(signUpRepository);
    }

    @Provides
    @Singleton
    SignUpRepository privideLoginRepository(EventBus eventBus, FirebaseAPI firebaseAPI){
        return new SignUpRepositoryImpl(eventBus, firebaseAPI);
    }
}
