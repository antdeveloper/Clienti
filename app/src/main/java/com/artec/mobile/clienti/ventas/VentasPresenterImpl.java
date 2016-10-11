package com.artec.mobile.clienti.ventas;

import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.ventas.events.VentasEvent;
import com.artec.mobile.clienti.ventas.ui.VentasView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public class VentasPresenterImpl implements VentasPresenter {
    private static final String EMPTY_LIST = "Listado vacio";
    private EventBus eventBus;
    private VentasView view;
    private VentasInteractor interactor;

    public VentasPresenterImpl(EventBus eventBus, VentasView view, VentasInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        this.view = null;
        eventBus.unregister(this);
    }

    @Override
    public void subscribe(String recipient) {
        if (view != null){
            view.hideList();
            view.showProgress();
        }
        interactor.subscribe(recipient);
    }

    @Override
    public void unsubscribe() {
        interactor.unsubscribe();
    }

    @Override
    public void removePhoto(Producto producto, Client client) {
        if (view != null){
            view.showProgress();
        }
        interactor.removePhoto(producto, client);
    }

    @Override
    @Subscribe
    public void onEventMainThread(VentasEvent event) {
        if (view != null){
            view.hideProgress();
            view.showList();
        }
        String error = event.getError();
        if (error != null){
            if (error.isEmpty()){
                view.onProductoError(EMPTY_LIST);
            }else{
                view.onProductoError(error);
            }
        }else{
            if (event.getType() == VentasEvent.READ_EVENT){
                view.addProducto(event.getProducto());
            }else if (event.getType() == VentasEvent.CHANGE_EVENT) {
                view.updateProducto(event.getProducto());
            }else if (event.getType() == VentasEvent.DELETE_EVENT){
                view.removeProducto(event.getProducto());
            }
        }
    }
}
