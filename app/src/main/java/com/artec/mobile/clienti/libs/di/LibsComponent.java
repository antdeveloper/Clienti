package com.artec.mobile.clienti.libs.di;

import com.artec.mobile.clienti.ClientiAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
@Singleton
@Component(modules = {LibsModule.class, ClientiAppModule.class})
public interface LibsComponent {
}
