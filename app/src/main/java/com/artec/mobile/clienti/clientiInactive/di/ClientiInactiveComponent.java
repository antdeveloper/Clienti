package com.artec.mobile.clienti.clientiInactive.di;

import com.artec.mobile.clienti.ClientiAppModule;
import com.artec.mobile.clienti.clientiInactive.ui.ClientiInactiveActivity;
import com.artec.mobile.clienti.domain.di.DomainModule;
import com.artec.mobile.clienti.libs.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ANICOLAS on 14/11/2016.
 */
@Singleton
@Component(modules = {ClientiInactiveModule.class, DomainModule.class, LibsModule.class, ClientiAppModule.class})
public interface ClientiInactiveComponent {
    void inject(ClientiInactiveActivity activity);
}
