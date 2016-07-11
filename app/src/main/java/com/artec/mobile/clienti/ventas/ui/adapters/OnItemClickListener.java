package com.artec.mobile.clienti.ventas.ui.adapters;

import android.widget.ImageView;

import com.artec.mobile.clienti.entities.Producto;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public interface OnItemClickListener {
    void onItemLongClick(Producto producto);
    void onShareClick(Producto producto, ImageView img);
    void onDeleteClick(Producto producto);
    void onAddAbonoClick(Producto producto);
}
