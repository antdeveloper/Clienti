package com.artec.mobile.clienti.main.di;

import com.artec.mobile.clienti.ClientiAppModule;
import com.artec.mobile.clienti.domain.di.DomainModule;
import com.artec.mobile.clienti.libs.di.LibsModule;
import com.artec.mobile.clienti.main.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
@Singleton
@Component(modules = {MainModule.class, DomainModule.class, LibsModule.class, ClientiAppModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
