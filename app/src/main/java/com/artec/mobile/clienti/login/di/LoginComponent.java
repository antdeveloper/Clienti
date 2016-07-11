package com.artec.mobile.clienti.login.di;

import com.artec.mobile.clienti.ClientiAppModule;
import com.artec.mobile.clienti.domain.di.DomainModule;
import com.artec.mobile.clienti.libs.di.LibsModule;
import com.artec.mobile.clienti.login.ui.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
@Singleton
@Component(modules = {LoginModule.class, DomainModule.class, LibsModule.class, ClientiAppModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
