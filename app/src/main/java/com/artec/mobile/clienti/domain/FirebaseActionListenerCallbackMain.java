package com.artec.mobile.clienti.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ANICOLAS on 28/06/2016.
 */
public interface FirebaseActionListenerCallbackMain {
    void onSuccess(HashMap<String, ArrayList> productos);
    void onError(String error);
}
