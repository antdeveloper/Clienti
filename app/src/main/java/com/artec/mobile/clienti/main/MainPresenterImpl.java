package com.artec.mobile.clienti.main;

import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.libs.base.EventBus;
import com.artec.mobile.clienti.main.events.MainEvent;
import com.artec.mobile.clienti.main.ui.MainView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ANICOLAS on 01/07/2016.
 */
public class MainPresenterImpl implements MainPresenter{
    private MainView view;
    private EventBus eventBus;
    private MainInteractor interactor;
    private MainSessionInteractor sessionInteractor;

    public MainPresenterImpl(MainView view, EventBus eventBus, MainInteractor interactor, MainSessionInteractor sessionInteractor) {
        this.view = view;
        this.eventBus = eventBus;
        this.interactor = interactor;
        this.sessionInteractor = sessionInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        eventBus.unregister(this);
        interactor.destroyListener();
    }

    @Override
    public void onPause() {
        interactor.unsubscribe();
    }

    @Override
    public void onResume() {
        interactor.subscribe();
    }

    @Override
    public void logout() {
        interactor.unsubscribe();
        interactor.destroyListener();
        sessionInteractor.signOff();
    }

    @Override
    @Subscribe
    public void onEventMainThread(MainEvent event) {
        Client client = event.getUser();
        switch (event.getType()){
            case MainEvent.CONTACT_ADDED:{
                onContactAdded(client);
                break;
            }
            case MainEvent.CONTACT_CHANGED:{
                onContactChanged(client);
                break;
            }
            case MainEvent.CONTACT_READ:{

                break;
            }
        }
    }

    private void onContactAdded(Client client){
        if (view != null){
            view.onClientAdded(client);
        }
    }

    private void onContactChanged(Client client){
        if (view != null){
            view.onClientChanged(client);
        }
    }
}
