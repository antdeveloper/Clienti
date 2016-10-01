package com.artec.mobile.clienti.domain;

import com.google.firebase.database.DatabaseError;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public interface FirebaseActionListenerCallback {
    void onSuccess();
    void onError(String error);
}
