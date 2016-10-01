package com.artec.mobile.clienti.productos;

import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.productos.events.ProductosEvent;
import com.artec.mobile.clienti.productos.ui.ProductosView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public class ProductosPresenterImpl implements ProductosPresenter {
    private ProductosView view;
    private EventBus eventBus;
    private ProductosUploadInteractor productosUploadInteractor;

    public ProductosPresenterImpl(ProductosView view, EventBus eventBus, ProductosUploadInteractor productosUploadInteractor) {
        this.view = view;
        this.eventBus = eventBus;
        this.productosUploadInteractor = productosUploadInteractor;
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
    public void uploadPhoto(Producto producto, Abono abono, String path, Client client) {
        productosUploadInteractor.execute(producto, abono, path, client);
    }

    @Override
    @Subscribe
    public void onEventMainThread(ProductosEvent event) {
        if (this.view != null){
            switch (event.getType()){
                case ProductosEvent.UPLOAD_INIT:{
                    view.onUploadInit();
                    break;
                }
                case ProductosEvent.UPLOAD_COMPLETE:{
                    view.onUploadComplete();
                    break;
                }
                case ProductosEvent.UPLOAD_ERROR:{
                    view.onUploadError(event.getError());
                    break;
                }
                case ProductosEvent.ABONO_ADDED:{
                    view.onAbonoAdded(event.getAbono());
                    break;
                }
            }
        }
    }
}
