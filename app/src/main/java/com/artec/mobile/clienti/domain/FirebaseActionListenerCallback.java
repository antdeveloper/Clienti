package com.artec.mobile.clienti.domain;

import com.firebase.client.FirebaseError;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public interface FirebaseActionListenerCallback {
    void onSuccess();
    void onError(FirebaseError error);
}
