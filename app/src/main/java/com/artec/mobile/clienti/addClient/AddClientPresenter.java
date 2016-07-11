package com.artec.mobile.clienti.addClient;

import com.artec.mobile.clienti.addClient.events.AddClientEvent;

/**
 * Created by ANICOLAS on 08/06/2016.
 */
public interface AddClientPresenter {
    void onShow();
    void onDestroy();

    void addContact(String email, String username);
    void onEventMainThread(AddClientEvent event);
}
