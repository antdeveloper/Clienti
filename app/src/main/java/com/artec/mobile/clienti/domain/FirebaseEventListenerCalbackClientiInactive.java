package com.artec.mobile.clienti.domain;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by ANICOLAS on 14/11/2016.
 */

public interface FirebaseEventListenerCalbackClientiInactive {
    void onGetChilds(DataSnapshot snapshot);
    void onCancelled(DatabaseError error);
}
