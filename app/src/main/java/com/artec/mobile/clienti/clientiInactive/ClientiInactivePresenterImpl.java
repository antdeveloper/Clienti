package com.artec.mobile.clienti.clientiInactive;

import com.artec.mobile.clienti.clientiInactive.events.ClientiInactiveEvent;
import com.artec.mobile.clienti.clientiInactive.ui.ClientiInactiveView;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.libs.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ANICOLAS on 14/11/2016.
 */

public class ClientiInactivePresenterImpl implements ClientiInactivePresenter {
    private ClientiInactiveView view;
    private EventBus eventBus;
    private ClientiInactiveInteractor interactor;

    public ClientiInactivePresenterImpl(ClientiInactiveView view, EventBus eventBus,
                                        ClientiInactiveInteractor interactor) {
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
    public void onGetClients() {
        showProgress();
        interactor.getClientsInactives();
    }

    @Override
    public void onReactiveClient(Client client) {
        showProgress();
        interactor.reactiveClient(client);
    }

    @Override
    public void onDeleteClient(Client client) {
        interactor.deleteClient(client);
    }

    @Override
    @Subscribe
    public void onEventMainThread(ClientiInactiveEvent evt) {
        if (view != null){
            view.hideProgress();

            switch (evt.getType()){
                case ClientiInactiveEvent.GET_CLIENTS:
                    view.onGetClients(evt.getClients());
                    break;
                case ClientiInactiveEvent.REACTIVED_CLIENT:
                case ClientiInactiveEvent.DELETED_CLIENT:
                    view.onClientDeleted(evt.getClient().getEmail());
                    break;
                case ClientiInactiveEvent.ERROR:
                case ClientiInactiveEvent.READ:
                    view.onError(evt.getType(), evt.getError());
                    break;
            }
        }
    }

    private void showProgress() {
        if (view != null){
            view.showProgress();
        }
    }
}
