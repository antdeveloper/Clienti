package com.artec.mobile.clienti.addAbono.di;

import com.artec.mobile.clienti.addAbono.AddAbonoInteractor;
import com.artec.mobile.clienti.addAbono.AddAbonoInteractorImpl;
import com.artec.mobile.clienti.addAbono.AddAbonoPresenter;
import com.artec.mobile.clienti.addAbono.AddAbonoPresenterImpl;
import com.artec.mobile.clienti.addAbono.AddAbonoRepository;
import com.artec.mobile.clienti.addAbono.AddAbonoRepositoryImpl;
import com.artec.mobile.clienti.addAbono.ui.AddAbonoView;
import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.libs.base.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 02/07/2016.
 */
@Module
public class AddAbonoModule {
    private AddAbonoView view;

    public AddAbonoModule(AddAbonoView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    AddAbonoView providesAddContactView(){
        return this.view;
    }

    @Provides @Singleton
    AddAbonoPresenter providesAddContactPresenter(EventBus eventBus, AddAbonoView view, AddAbonoInteractor interactor){
        return new AddAbonoPresenterImpl(eventBus, view, interactor);
    }

    @Provides @Singleton
    AddAbonoInteractor providesAddContactInteractor(AddAbonoRepository repository){
        return new AddAbonoInteractorImpl(repository);
    }

    @Provides @Singleton
    AddAbonoRepository providesAddContactRepository(EventBus eventBus, FirebaseAPI firebaseAPI){
        return new AddAbonoRepositoryImpl(eventBus, firebaseAPI);
    }
}
