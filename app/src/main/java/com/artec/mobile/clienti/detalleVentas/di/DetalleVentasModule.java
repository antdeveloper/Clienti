package com.artec.mobile.clienti.detalleVentas.di;

import com.artec.mobile.clienti.detalleVentas.DetalleVentaInteractor;
import com.artec.mobile.clienti.detalleVentas.DetalleVentaInteractorImpl;
import com.artec.mobile.clienti.detalleVentas.DetalleVentaPresenter;
import com.artec.mobile.clienti.detalleVentas.DetalleVentaPresenterImpl;
import com.artec.mobile.clienti.detalleVentas.DetalleVentaRepository;
import com.artec.mobile.clienti.detalleVentas.DetalleVentaRepositoryImpl;
import com.artec.mobile.clienti.detalleVentas.ui.DetalleVentaView;
import com.artec.mobile.clienti.detalleVentas.ui.adapters.AbonoAdapter;
import com.artec.mobile.clienti.detalleVentas.ui.adapters.OnAbonoClickListener;
import com.artec.mobile.clienti.domain.FirebaseAPI;
import com.artec.mobile.clienti.entities.Abono;
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
public class DetalleVentasModule {
    private DetalleVentaView view;
    private OnAbonoClickListener onItemClickListener;

    public DetalleVentasModule(DetalleVentaView view, OnAbonoClickListener onItemClickListener) {
        this.view = view;
        this.onItemClickListener = onItemClickListener;
    }

    @Provides
    @Singleton
    DetalleVentaView providesPhotoListView(){
        return this.view;
    }

    @Provides @Singleton
    DetalleVentaPresenter providesPhotoListPresenter(EventBus eventBus, DetalleVentaView view, DetalleVentaInteractor interactor){
        return new DetalleVentaPresenterImpl(view, eventBus, interactor);
    }

    @Provides @Singleton
    DetalleVentaInteractor providesPhotoListInteractor(DetalleVentaRepository reposiroty){
        return new DetalleVentaInteractorImpl(reposiroty);
    }

    @Provides @Singleton
    DetalleVentaRepository providesPhotoListReposiroty(EventBus eventBus, FirebaseAPI firebaseAPI, ImageStorage imageStorage){
        return new DetalleVentaRepositoryImpl(eventBus, firebaseAPI, imageStorage);
    }

    @Provides @Singleton
    AbonoAdapter providesPhotoListAdapter(List<Abono> productoList, OnAbonoClickListener onItemClickListener){
        return new AbonoAdapter(productoList, onItemClickListener);
    }

    /*@Provides @Singleton
    DetalleVentaActivity providesDetalleVentaActivity(ImageLoader imageLoader){
        return new DetalleVentaActivity(imageLoader);
    }*/

    @Provides @Singleton
    OnAbonoClickListener providesOnItemClickListener(){
        return this.onItemClickListener;
    }

    @Provides @Singleton
    List<Abono> providesPhotoList(){
        return new ArrayList<>();
    }
}