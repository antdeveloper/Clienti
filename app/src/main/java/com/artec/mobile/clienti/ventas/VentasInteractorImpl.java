package com.artec.mobile.clienti.ventas;

import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.ventas.events.VentasEvent;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public class VentasInteractorImpl implements VentasInteractor {
    private VentasReposiroty reposiroty;

    public VentasInteractorImpl(VentasReposiroty reposiroty) {
        this.reposiroty = reposiroty;
    }

    @Override
    public void subscribe(String recipient) {
        reposiroty.subscribe(recipient);
    }

    @Override
    public void unsubscribe() {
        reposiroty.unsubscribe();
    }

    @Override
    public void removePhoto(Producto producto, Client client) {
        reposiroty.removePhoto(producto, client);
    }

    @Override
    public void onEventMainThread(VentasEvent event) {

    }
}
