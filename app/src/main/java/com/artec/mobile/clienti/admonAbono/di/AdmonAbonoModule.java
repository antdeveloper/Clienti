package com.artec.mobile.clienti.admonAbono.di;

import com.artec.mobile.clienti.admonAbono.AdmonAbonoInteractor;
import com.artec.mobile.clienti.admonAbono.AdmonAbonoInteractorImpl;
import com.artec.mobile.clienti.admonAbono.AdmonAbonoPresenter;
import com.artec.mobile.clienti.admonAbono.AdmonAbonoPresenterImpl;
import com.artec.mobile.clienti.admonAbono.AdmonAbonoRepository;
import com.artec.mobile.clienti.admonAbono.AdmonAbonoRepositoryImpl;
import com.artec.mobile.clienti.admonAbono.ui.AdmonAbonoView;
import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.libs.base.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 02/07/2016.
 */
@Module
public class AdmonAbonoModule {
    private AdmonAbonoView view;

    public AdmonAbonoModule(AdmonAbonoView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    AdmonAbonoView providesAddContactView(){
        return this.view;
    }

    @Provides @Singleton
    AdmonAbonoPresenter providesAddContactPresenter(EventBus eventBus, AdmonAbonoView view, AdmonAbonoInteractor interactor){
        return new AdmonAbonoPresenterImpl(eventBus, view, interactor);
    }

    @Provides @Singleton
    AdmonAbonoInteractor providesAddContactInteractor(AdmonAbonoRepository repository){
        return new AdmonAbonoInteractorImpl(repository);
    }

    @Provides @Singleton
    AdmonAbonoRepository providesAddContactRepository(EventBus eventBus, FirebaseAPI firebaseAPI){
        return new AdmonAbonoRepositoryImpl(eventBus, firebaseAPI);
    }
}
