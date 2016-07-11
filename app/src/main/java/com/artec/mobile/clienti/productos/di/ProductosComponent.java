package com.artec.mobile.clienti.productos.di;

import com.artec.mobile.clienti.ClientiAppModule;
import com.artec.mobile.clienti.domain.di.DomainModule;
import com.artec.mobile.clienti.libs.di.LibsModule;
import com.artec.mobile.clienti.productos.ui.ProductosActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
@Singleton
@Component(modules = {ProductosModule.class, DomainModule.class, LibsModule.class, ClientiAppModule.class})
public interface ProductosComponent {
    void inject(ProductosActivity activity);
}
