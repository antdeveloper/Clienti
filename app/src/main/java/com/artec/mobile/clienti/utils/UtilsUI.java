package com.artec.mobile.clienti.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by ANICOLAS on 17/11/2016.
 */

public class UtilsUI {

    public static void hideKeyboard(Activity context){
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static void hideKeyboard(Activity context, EditText editText){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity context, EditText editText){// FIXME: 30/11/2016
        /*InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/

        InputMethodManager in = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }
}
