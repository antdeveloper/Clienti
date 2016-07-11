package com.artec.mobile.clienti.domain;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public interface FirebaseEventListenerCallbackVentas {
    void onChildAdded(DataSnapshot snapshot);
    void onChildRemoved(DataSnapshot snapshot);
    void onCancelled(FirebaseError error);
    void onChildUpdated(DataSnapshot snapshot);
}
