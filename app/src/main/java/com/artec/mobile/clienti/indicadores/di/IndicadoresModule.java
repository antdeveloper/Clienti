package com.artec.mobile.clienti.indicadores.di;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.indicadores.IndicadoresInteractor;
import com.artec.mobile.clienti.indicadores.IndicadoresInteractorImpl;
import com.artec.mobile.clienti.indicadores.IndicadoresPresenter;
import com.artec.mobile.clienti.indicadores.IndicadoresPresenterImpl;
import com.artec.mobile.clienti.indicadores.IndicadoresRepository;
import com.artec.mobile.clienti.indicadores.IndicadoresRepositoryImpl;
import com.artec.mobile.clienti.indicadores.ui.IndicadoresView;
import com.artec.mobile.clienti.libs.base.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 18/11/2016.
 */
@Module
public class IndicadoresModule {
    private IndicadoresView view;

    public IndicadoresModule(IndicadoresView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    IndicadoresView providesIndicadoresView(){
        return this.view;
    }

    @Provides @Singleton
    IndicadoresPresenter providesIndicadoresPresenter(IndicadoresView view, EventBus eventBus,
                                                      IndicadoresInteractor interactor){
        return new IndicadoresPresenterImpl(view, eventBus, interactor);
    }

    @Provides @Singleton
    IndicadoresInteractor providesIndicadoresInteractor(IndicadoresRepository repository){
        return new IndicadoresInteractorImpl(repository);
    }

    @Provides @Singleton
    IndicadoresRepository providesIndicadoresRepository(EventBus eventBus, FirebaseAPI firebaseAPI){
        return new IndicadoresRepositoryImpl(eventBus, firebaseAPI);
    }
}
