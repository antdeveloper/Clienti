package com.artec.mobile.clienti.addClient.di;

import com.artec.mobile.clienti.ClientiAppModule;
import com.artec.mobile.clienti.addClient.ui.AddClientFragment;
import com.artec.mobile.clienti.domain.di.DomainModule;
import com.artec.mobile.clienti.libs.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
@Singleton
@Component(modules = {AddClientModule.class, DomainModule.class, LibsModule.class, ClientiAppModule.class})
public interface AddClientComponent {
    void inject(AddClientFragment dialogFragment);
}
