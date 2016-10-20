package com.artec.mobile.clienti.detalleVentas;

import com.artec.mobile.clienti.detalleVentas.events.DetalleVentaEvent;
import com.artec.mobile.clienti.detalleVentas.ui.DetalleVentaView;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ANICOLAS on 18/10/2016.
 */

public class DetalleVentaPresenterImpl implements DetalleVentaPresenter {
    private DetalleVentaView view;
    private EventBus eventBus;
    private DetalleVentaInteractor interactor;

    public DetalleVentaPresenterImpl(DetalleVentaView view, EventBus eventBus, DetalleVentaInteractor interactor) {
        this.view = view;
        this.eventBus = eventBus;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        eventBus.unregister(this);
    }

    @Override
    public void update(Producto producto, String path, String clientEmail) {
        inProgress();
        interactor.execute(producto, path, clientEmail);
    }

    @Override
    @Subscribe
    public void onEventMainThread(DetalleVentaEvent event) {
        if (view != null){
            view.enableUIElements();
            view.hideProgress();

            switch (event.getType()){
                case DetalleVentaEvent.INIT:{
                    view.productInit();
                    break;
                }
                case DetalleVentaEvent.SUCCESS:{
                    view.productUpdate();
                    break;
                }
                case DetalleVentaEvent.ERROR:{
                    view.onError(event.getError());
                    break;
                }
            }
        }
    }

    private void inProgress(){
        if (view != null){
            view.showProgress();
            view.disableUIElements();
        }
    }
}
