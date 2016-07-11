package com.artec.mobile.clienti.main.di;

import android.app.Activity;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.domain.Util;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.libs.base.ImageLoader;
import com.artec.mobile.clienti.main.MainInteractor;
import com.artec.mobile.clienti.main.MainInteractorImpl;
import com.artec.mobile.clienti.main.MainPresenter;
import com.artec.mobile.clienti.main.MainPresenterImpl;
import com.artec.mobile.clienti.main.MainRepository;
import com.artec.mobile.clienti.main.MainRepositoryImpl;
import com.artec.mobile.clienti.main.MainSessionInteractor;
import com.artec.mobile.clienti.main.MainSessionInteractorImpl;
import com.artec.mobile.clienti.main.ui.MainActivity;
import com.artec.mobile.clienti.main.ui.MainView;
import com.artec.mobile.clienti.main.ui.adapters.MainAdapter;
import com.artec.mobile.clienti.main.ui.adapters.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 02/07/2016.
 */
@Module
public class MainModule {
    private MainView view;
    private OnItemClickListener onItemClickListener;
    private MainActivity activity;

    public MainModule(MainView view, OnItemClickListener onItemClickListener) {
        this.view = view;
        this.onItemClickListener = onItemClickListener;
    }

    @Provides
    @Singleton
    MainView providesMainView(){
        return this.view;
    }

    @Provides @Singleton
    MainPresenter providesMainPresenter(MainView view, EventBus eventBus, MainInteractor interactor,
                                        MainSessionInteractor sessionInteractor){
        return new MainPresenterImpl(view, eventBus, interactor, sessionInteractor);
    }

    @Provides @Singleton
    MainInteractor providesMainInteractor(MainRepository repository){
        return new MainInteractorImpl(repository);
    }

    @Provides @Singleton
    MainSessionInteractor providesMainSessionInteractor(MainRepository repository){
        return new MainSessionInteractorImpl(repository);
    }

    @Provides @Singleton
    MainRepository providesMainRepository(EventBus eventBus, FirebaseAPI firebaseAPI){
        return new MainRepositoryImpl(eventBus, firebaseAPI);
    }

    @Provides @Singleton
    MainAdapter providesMainAdapter(Util util, List<Client> clientList, ImageLoader imageLoader,
                                    OnItemClickListener onItemClickListener){
        return new MainAdapter(util, clientList, imageLoader, onItemClickListener);
    }

    @Provides @Singleton
    OnItemClickListener providesOnItemClickListener(){
        return this.onItemClickListener;
    }

    @Provides @Singleton
    List<Client> providesUserList(){
        return new ArrayList<>();
    }
}
