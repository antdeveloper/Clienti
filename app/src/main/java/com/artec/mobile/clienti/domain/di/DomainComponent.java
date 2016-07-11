package com.artec.mobile.clienti.domain.di;

import com.artec.mobile.clienti.ClientiAppModule;

import javax.inject.Singleton;

import dagger.Component;
/**
 * Created by ANICOLAS on 28/06/2016.
 */
@Singleton
@Component(modules = {DomainModule.class, ClientiAppModule.class})
public interface DomainComponent {
}
