package com.artec.mobile.clienti.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ANICOLAS on 17/11/2016.
 */

public class UtilsValidator {

    public static boolean isInvalidDouble(String numStr) {
        try{
            Double result = Double.valueOf(numStr);
            return false;
        } catch (Exception e){
            return true;
        }
    }
}
