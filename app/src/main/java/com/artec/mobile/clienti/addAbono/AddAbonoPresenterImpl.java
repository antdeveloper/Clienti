package com.artec.mobile.clienti.addAbono;

import com.artec.mobile.clienti.addAbono.events.AddAbonoEvent;
import com.artec.mobile.clienti.addAbono.ui.AddAbonoView;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AddAbonoPresenterImpl implements AddAbonoPresenter {
    private EventBus eventBus;
    private AddAbonoView view;
    private AddAbonoInteractor interactor;

    public AddAbonoPresenterImpl(EventBus eventBus, AddAbonoView view, AddAbonoInteractor interactor) {
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
        if (view != null){
            view.hideInput();
            view.showProgress();
        }
        interactor.execute(producto, abono, client);
    }

    @Override
    public void addAbonoGral(List<Producto> productos, double abonoGral, long fecha, Client client) {
        if (view != null){
            view.hideInput();
            view.showProgress();
        }
        interactor.addAbonoGral(productos, abonoGral, fecha, client);
    }

    @Override
    @Subscribe
    public void onEventMainThread(AddAbonoEvent event) {
        if (view != null){
            view.hideProgress();
            view.showInput();

            if (event.isError()){
                view.abonoNotAdded();
            }else {
                view.abonoAdded();
            }
        }
    }
}
