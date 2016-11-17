package com.artec.mobile.clienti.clientiInactive.di;

import com.artec.mobile.clienti.clientiInactive.ClientiInactiveInteractor;
import com.artec.mobile.clienti.clientiInactive.ClientiInactiveInteractorImpl;
import com.artec.mobile.clienti.clientiInactive.ClientiInactivePresenter;
import com.artec.mobile.clienti.clientiInactive.ClientiInactivePresenterImpl;
import com.artec.mobile.clienti.clientiInactive.ClientiInactiveRepository;
import com.artec.mobile.clienti.clientiInactive.ClientiInactiveRepositoryImpl;
import com.artec.mobile.clienti.clientiInactive.ui.ClientiInactiveView;
import com.artec.mobile.clienti.clientiInactive.ui.adapters.ClientiInactiveAdapter;
import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.Util;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.libs.base.ImageLoader;
import com.artec.mobile.clienti.main.ui.adapters.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 14/11/2016.
 */
@Module
public class ClientiInactiveModule {
    private ClientiInactiveView view;
    private OnItemClickListener onItemClickListener;

    public ClientiInactiveModule(ClientiInactiveView view, OnItemClickListener onItemClickListener) {
        this.view = view;
        this.onItemClickListener = onItemClickListener;
    }

    @Provides
    @Singleton
    ClientiInactiveView providesClientiInactiveView(){
        return this.view;
    }

    @Provides @Singleton
    ClientiInactivePresenter prodivesClientiInactivePresenter(ClientiInactiveView view, EventBus eventBus,
                                                              ClientiInactiveInteractor interactor){
        return new ClientiInactivePresenterImpl(view, eventBus, interactor);
    }

    @Provides @Singleton
    ClientiInactiveInteractor providesClientiInactiveInteractor(ClientiInactiveRepository repository){
        return new ClientiInactiveInteractorImpl(repository);
    }

    @Provides @Singleton
    ClientiInactiveRepository providesClientiInactiveRepository(EventBus eventBus, FirebaseAPI firebase){
        return new ClientiInactiveRepositoryImpl(eventBus, firebase);
    }

    @Provides @Singleton
    ClientiInactiveAdapter providesClientiInactiveAdapter(Util util, List<Client> clients, ImageLoader imageLoader,
                                                          OnItemClickListener onItemClickListener){
        return new ClientiInactiveAdapter(util, clients, imageLoader, onItemClickListener);
    }

    @Provides @Singleton
    OnItemClickListener providesOnItemClickListener(){
        return this.onItemClickListener;
    }

    @Provides @Singleton
    List<Client> providesClients(){
        return new ArrayList<>();
    }
}
