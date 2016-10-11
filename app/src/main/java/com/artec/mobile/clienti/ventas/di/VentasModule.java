package com.artec.mobile.clienti.ventas.di;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.libs.base.ImageLoader;
import com.artec.mobile.clienti.libs.base.ImageStorage;
import com.artec.mobile.clienti.ventas.VentasInteractor;
import com.artec.mobile.clienti.ventas.VentasInteractorImpl;
import com.artec.mobile.clienti.ventas.VentasPresenter;
import com.artec.mobile.clienti.ventas.VentasPresenterImpl;
import com.artec.mobile.clienti.ventas.VentasReposiroty;
import com.artec.mobile.clienti.ventas.VentasRepositoryImpl;
import com.artec.mobile.clienti.ventas.ui.VentasView;
import com.artec.mobile.clienti.ventas.ui.adapters.OnItemClickListener;
import com.artec.mobile.clienti.ventas.ui.adapters.VentasAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
@Module
public class VentasModule {
    private VentasView view;
    private OnItemClickListener onItemClickListener;

    public VentasModule(VentasView view, OnItemClickListener onItemClickListener) {
        this.view = view;
        this.onItemClickListener = onItemClickListener;
    }

    @Provides
    @Singleton
    VentasView providesPhotoListView(){
        return this.view;
    }

    @Provides @Singleton
    VentasPresenter providesPhotoListPresenter(EventBus eventBus, VentasView view, VentasInteractor interactor){
        return new VentasPresenterImpl(eventBus, view, interactor);
    }

    @Provides @Singleton
    VentasInteractor providesPhotoListInteractor(VentasReposiroty reposiroty){
        return new VentasInteractorImpl(reposiroty);
    }

    @Provides @Singleton
    VentasReposiroty providesPhotoListReposiroty(EventBus eventBus, FirebaseAPI firebaseAPI, ImageStorage imageStorage){
        return new VentasRepositoryImpl(eventBus, firebaseAPI, imageStorage);
    }

    @Provides @Singleton
    VentasAdapter providesPhotoListAdapter(List<Producto> productoList, ImageLoader imageLoader, OnItemClickListener onItemClickListener){
        return new VentasAdapter(productoList, imageLoader, onItemClickListener);
    }

    /*@Provides @Singleton
    DetalleVentaActivity providesDetalleVentaActivity(ImageLoader imageLoader){
        return new DetalleVentaActivity(imageLoader);
    }*/

    @Provides @Singleton
    OnItemClickListener providesOnItemClickListener(){
        return this.onItemClickListener;
    }

    @Provides @Singleton
    List<Producto> providesPhotoList(){
        return new ArrayList<>();
    }
}