package com.artec.mobile.clienti.addClient.di;

import com.artec.mobile.clienti.addClient.AddClientInteractor;
import com.artec.mobile.clienti.addClient.AddClientPresenter;
import com.artec.mobile.clienti.addClient.AddClientPresenterImpl;
import com.artec.mobile.clienti.addClient.AddClientRepository;
import com.artec.mobile.clienti.addClient.AddClientInteractorImpl;
import com.artec.mobile.clienti.addClient.AddClientRepositoryImpl;
import com.artec.mobile.clienti.addClient.ui.AddClientView;
import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.libs.base.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 02/07/2016.
 */
@Module
public class AddClientModule {
    private AddClientView view;

    public AddClientModule(AddClientView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    AddClientView providesAddContactView(){
        return this.view;
    }

    @Provides @Singleton
    AddClientPresenter providesAddContactPresenter(EventBus eventBus, AddClientView view, AddClientInteractor interactor){
        return new AddClientPresenterImpl(eventBus, view, interactor);
    }

    @Provides @Singleton
    AddClientInteractor providesAddContactInteractor(AddClientRepository repository){
        return new AddClientInteractorImpl(repository);
    }

    @Provides @Singleton
    AddClientRepository providesAddContactRepository(EventBus eventBus, FirebaseAPI firebaseAPI){
        return new AddClientRepositoryImpl(eventBus, firebaseAPI);
    }
}
