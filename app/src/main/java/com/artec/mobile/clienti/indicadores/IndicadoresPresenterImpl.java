package com.artec.mobile.clienti.indicadores;

import com.artec.mobile.clienti.indicadores.events.IndicadoresEvent;
import com.artec.mobile.clienti.indicadores.ui.IndicadoresView;
import com.artec.mobile.clienti.libs.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ANICOLAS on 18/11/2016.
 */

public class IndicadoresPresenterImpl implements IndicadoresPresenter {
    private IndicadoresView view;
    private EventBus eventBus;
    private IndicadoresInteractor interactor;

    public IndicadoresPresenterImpl(IndicadoresView view, EventBus eventBus, IndicadoresInteractor interactor) {
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
    public void onGetProducts(String recipient) {
        if (view != null){
            view.showProgress();
            interactor.getProducts(recipient);
        }
    }

    @Override
    @Subscribe
    public void onEventMainThread(IndicadoresEvent event) {
        if (view != null){
            view.hideProgress();

            switch (event.getType()){
                case IndicadoresEvent.SUCCESS:{
                    view.onGetProducts(event.getProductos());
                    break;
                }
                case IndicadoresEvent.READ:
                case IndicadoresEvent.ERROR:{
                    view.onError(event.getType(), event.getError());
                    break;
                }
            }
        }
    }
}
