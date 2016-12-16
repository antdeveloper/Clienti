package com.artec.mobile.clienti.indicadores.di;

import com.artec.mobile.clienti.ClientiAppModule;
import com.artec.mobile.clienti.domain.di.DomainModule;
import com.artec.mobile.clienti.indicadores.ui.IndicadoresActivity;
import com.artec.mobile.clienti.libs.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ANICOLAS on 18/11/2016.
 */
@Singleton
@Component(modules = {IndicadoresModule.class, DomainModule.class, LibsModule.class, ClientiAppModule.class})
public interface IndicadoresComponent {
    void inject(IndicadoresActivity activity);
}
