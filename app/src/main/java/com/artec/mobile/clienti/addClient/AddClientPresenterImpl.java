package com.artec.mobile.clienti.addClient;

import com.artec.mobile.clienti.addClient.events.AddClientEvent;
import com.artec.mobile.clienti.addClient.ui.AddClientView;
import com.artec.mobile.clienti.libs.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AddClientPresenterImpl implements AddClientPresenter {
    private EventBus eventBus;
    private AddClientView view;
    private AddClientInteractor interactor;

    public AddClientPresenterImpl(EventBus eventBus, AddClientView view, AddClientInteractor interactor) {
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
    public void addContact(String email, String username, String customUsername) {
        if (view != null){
            view.hideInput();
            view.showProgress();
        }
        interactor.execute(email, username, customUsername);
    }

    @Override
    @Subscribe
    public void onEventMainThread(AddClientEvent event) {
        if (view != null){
            view.hideProgress();
            view.showInput();

            if (event.isError()){
                view.contactNotAdded();
            }else {
                view.contactAdded();
            }
        }
    }
}
