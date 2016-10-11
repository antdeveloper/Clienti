package com.artec.mobile.clienti.libs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by ANICOLAS on 16/08/2016.
 */
public class Utilities {

    public static boolean isOnline(Context context){
        ConnectivityManager manager;
        NetworkInfo info;
        manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        info = manager.getActiveNetworkInfo();

        if (info != null){
            ConnectivityManager managerActive = (ConnectivityManager)context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo infoActive = managerActive.getActiveNetworkInfo();

            if (infoActive.isConnected()){
                return true;
            }
        }else{
            return false;
        }

        return false;
    }

    public static boolean materialDesign() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
