package com.artec.mobile.clienti.detalleVentas.di;

import com.artec.mobile.clienti.ClientiAppModule;
import com.artec.mobile.clienti.domain.di.DomainModule;
import com.artec.mobile.clienti.libs.di.LibsModule;
import com.artec.mobile.clienti.detalleVentas.ui.DetalleVentaActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
@Singleton
@Component(modules = {DetalleVentasModule.class, DomainModule.class, LibsModule.class, ClientiAppModule.class})
public interface DetalleVentaComponent {
    void inject(DetalleVentaActivity activity);
}
