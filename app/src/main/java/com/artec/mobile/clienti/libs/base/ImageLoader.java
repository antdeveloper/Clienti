package com.artec.mobile.clienti.libs.base;

import android.widget.ImageView;

/**
 * Created by ANICOLAS on 16/06/2016.
 */
public interface ImageLoader {
    void load(ImageView imageView, String URL);
    void setOnFinishedImageLoadingListener(Object listener);
}
