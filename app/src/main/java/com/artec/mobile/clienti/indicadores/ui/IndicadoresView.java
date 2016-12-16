package com.artec.mobile.clienti.indicadores.ui;

import com.artec.mobile.clienti.entities.Producto;

import java.util.List;

/**
 * Created by ANICOLAS on 18/11/2016.
 */

public interface IndicadoresView {
    void showProgress();
    void hideProgress();

    void onGetProducts(List<Producto> productos);
    void onError(int type, String error);
}
