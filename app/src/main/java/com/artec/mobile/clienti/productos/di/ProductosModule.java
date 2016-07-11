package com.artec.mobile.clienti.productos.di;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.libs.base.ImageStorage;
import com.artec.mobile.clienti.productos.ProductosPresenter;
import com.artec.mobile.clienti.productos.ProductosPresenterImpl;
import com.artec.mobile.clienti.productos.ProductosRepository;
import com.artec.mobile.clienti.productos.ProductosUploadInteractor;
import com.artec.mobile.clienti.productos.ProductosUploadInteractorImpl;
import com.artec.mobile.clienti.productos.ProductosRepositoryImpl;
import com.artec.mobile.clienti.productos.ui.ProductosView;
import com.artec.mobile.clienti.productos.ui.adapters.ProductosSectionPageAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
@Module
public class ProductosModule {
    private ProductosView view;
    private String[] titles;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;

    public ProductosModule(ProductosView view, String[] titles, Fragment[] fragments, FragmentManager fragmentManager) {
        this.view = view;
        this.titles = titles;
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
    }

    @Provides
    @Singleton
    ProductosView providesVentasMainView(){
        return this.view;
    }

    @Provides @Singleton
    ProductosPresenter providesVentasMainPresenter(ProductosView view, EventBus eventBus, ProductosUploadInteractor productosUploadInteractor){
        return new ProductosPresenterImpl(view, eventBus, productosUploadInteractor);
    }

    @Provides @Singleton
    ProductosUploadInteractor providesUploadInteractor(ProductosRepository repository){
        return new ProductosUploadInteractorImpl(repository);
    }

    @Provides @Singleton
    ProductosRepository providesMainRepository(EventBus eventBus, FirebaseAPI firebase, ImageStorage imageStorage){
        return new ProductosRepositoryImpl(eventBus, firebase, imageStorage);
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
