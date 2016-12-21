package com.artec.mobile.clienti.indicadores.di;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.indicadores.IndicadoresInteractor;
import com.artec.mobile.clienti.indicadores.IndicadoresInteractorImpl;
import com.artec.mobile.clienti.indicadores.IndicadoresPresenter;
import com.artec.mobile.clienti.indicadores.IndicadoresPresenterImpl;
import com.artec.mobile.clienti.indicadores.IndicadoresRepository;
import com.artec.mobile.clienti.indicadores.IndicadoresRepositoryImpl;
import com.artec.mobile.clienti.indicadores.ui.IndicadoresView;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.productos.ui.adapters.ProductosSectionPageAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 18/11/2016.
 */
@Module
public class IndicadoresModule {
    private IndicadoresView view;
    private String[] titles;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;

    public IndicadoresModule(IndicadoresView view, String[] titles, Fragment[] fragments,
                             FragmentManager fragmentManager) {
        this.view = view;
        this.titles = titles;
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
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

    @Provides @Singleton
    ProductosSectionPageAdapter providesMainSectionPageAdapter(FragmentManager fm, String[] titles, Fragment[] fragments){
        return new ProductosSectionPageAdapter(fm, titles, fragments);
    }

    @Provides @Singleton
    FragmentManager providesFragmentManager(){
        return this.fragmentManager;
    }

    @Provides @Singleton
    Fragment[] providesFragmentArrayForAdapter(){
        return this.fragments;
    }

    @Provides @Singleton
    String[] providesStringArrayForAdapter(){
        return this.titles;
    }
}
