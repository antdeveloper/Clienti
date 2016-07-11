package com.artec.mobile.clienti.signup.di;

import com.artec.mobile.clienti.ClientiAppModule;
import com.artec.mobile.clienti.domain.di.DomainModule;
import com.artec.mobile.clienti.libs.di.LibsModule;
import com.artec.mobile.clienti.signup.ui.SignUpActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
@Singleton
@Component(modules = {SignUpModule.class, DomainModule.class, LibsModule.class, ClientiAppModule.class})
public interface SignUpComponent {
    void inject(SignUpActivity activity);
}
