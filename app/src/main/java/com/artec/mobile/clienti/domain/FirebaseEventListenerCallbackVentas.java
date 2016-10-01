package com.artec.mobile.clienti.domain;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public interface FirebaseEventListenerCallbackVentas {
    void onChildAdded(DataSnapshot snapshot);
    void onChildRemoved(DataSnapshot snapshot);
    void onCancelled(DatabaseError error);
    void onChildUpdated(DataSnapshot snapshot);
}
