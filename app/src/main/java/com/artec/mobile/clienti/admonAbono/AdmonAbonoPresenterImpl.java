package com.artec.mobile.clienti.admonAbono;

import com.artec.mobile.clienti.admonAbono.events.AdmonAbonoEvent;
import com.artec.mobile.clienti.admonAbono.ui.AdmonAbonoView;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AdmonAbonoPresenterImpl implements AdmonAbonoPresenter {
    private EventBus eventBus;
    private AdmonAbonoView view;
    private AdmonAbonoInteractor interactor;

    public AdmonAbonoPresenterImpl(EventBus eventBus, AdmonAbonoView view, AdmonAbonoInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onShow() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        eventBus.unregister(this);
    }

    @Override
    public void addAbono(Producto producto, Abono abono, Client client) {
        setViewProgress();
        interactor.execute(producto, abono, client);
    }

    @Override
    public void updateAbono(Producto producto, Abono abono, Client client) {
        setViewProgress();
        interactor.updateAbono(producto, abono, client);
    }

    @Override
    public void addAbonoGral(List<Producto> productos, double abonoGral, long fecha, Client client) {
        setViewProgress();
        interactor.addAbonoGral(productos, abonoGral, fecha, client);
    }

    @Override
    public void deleteAbono(Producto producto, Abono abono, Client client) {
        setViewProgress();
        interactor.deleteAbono(producto, abono, client);
    }

    private void setViewProgress() {
        if (view != null){
            view.hideInput();
            view.showProgress();
        }
    }

    @Override
    @Subscribe
    public void onEventMainThread(AdmonAbonoEvent event) {
        if (view != null){
            view.hideProgress();
            view.showInput();

            switch (event.getType()){
                case AdmonAbonoEvent.SUCCESS:{
                    view.abonoAdded(event.getAbono());
                    break;
                }
                case AdmonAbonoEvent.ABONO_UPDATED:{
                    view.abonoUpdated();
                    break;
                }
                case AdmonAbonoEvent.ABONO_DELETED:{
                    view.abonoDeleted();
                    break;
                }
                default:{
                    view.abonoNotAdded();
                    break;
                }
            }
        }
    }
}
