package com.artec.mobile.clienti.ventas.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.base.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ANICOLAS on 29/06/2016.
 */
public class VentasAdapter extends RecyclerView.Adapter<VentasAdapter.ViewHolder> {
    private List<Producto> productoList;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;

    private Context context;

    public VentasAdapter(List<Producto> productoList, ImageLoader imageLoader, OnItemClickListener onItemClickListener) {
        this.productoList = productoList;
        this.imageLoader = imageLoader;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_producto, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Producto currentProducto = productoList.get(position);
        double total = currentProducto.getPrecio()* currentProducto.getCantidad();
        holder.setOnItemClickListener(currentProducto, onItemClickListener);
        if (currentProducto.getUrl().isEmpty()) {
            holder.imgMain.setImageResource(R.drawable.ic_file_image);
        }else {
            imageLoader.load(holder.imgMain, currentProducto.getUrl());
        }
        holder.txtName.setText( context.getString(R.string.productos_hint_name) +": " +
                currentProducto.getName());
        holder.txtModel.setText( context.getString(R.string.productos_hint_model) + ": " +
                currentProducto.getModelo());
        holder.txtTotal.setText(String.format(Locale.ROOT, context.getString(
                R.string.ventas_property_total), total));
        /*holder.txtAbono.setText( context.getString(R.string.ventas_property_pagado) + ": $" +
                currentProducto.getAbono());*/
        holder.txtAdeudo.setText(String.format(Locale.ROOT, context.getString(
                R.string.ventas_property_adeudo), total- currentProducto.getAbono()));
        holder.txtCantidad.setText(context.getString(R.string.ventas_property_cantidad) + ": (x" +
                String.format(Locale.getDefault(), "%d", currentProducto.getCantidad()) + ")");
        holder.txtFechaVenta.setText(new SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                .format(currentProducto.getFechaVenta()));
    }

    public void addPhoto(Producto producto) {
        productoList.add(0, producto);
        notifyDataSetChanged();
    }

    public void update(Producto producto){
        int index = getIndexById(producto);
        productoList.set(index, producto);
        notifyDataSetChanged();
    }

    private int getIndexById(Producto producto) {
        for (int i = 0; i< productoList.size(); i++){
            if (productoList.get(i).getId().equals(producto.getId())){
                return i;
            }
        }
        return -1;
    }

    public void removePhoto(Producto producto) {
        int index = getIndexById(producto);
        productoList.remove(index);
        notifyDataSetChanged();
    }

    public double getAdeudoTotal(){
        double adeudo = 0;
        for (Producto producto : productoList){
            adeudo += producto.getAdeudo();
        }
        return adeudo;
    }

    public List<Producto> getProductos(){
        return productoList;
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imgMain)
        ImageView imgMain;
        @Bind(R.id.txtName)
        TextView txtName;
        @Bind(R.id.txtModel)
        TextView txtModel;
        @Bind(R.id.txtTotal)
        TextView txtTotal;
        @Bind(R.id.txtAbono)
        TextView txtAbono;
        @Bind(R.id.txtAdeudo)
        TextView txtAdeudo;
        @Bind(R.id.imgAddAbono)
        ImageButton imgAddAbono;
        @Bind(R.id.txtCantidad)
        TextView txtCantidad;

        @Bind(R.id.txtFechaVenta)
        TextView txtFechaVenta;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }

        public void setOnItemClickListener(final Producto producto, final OnItemClickListener listener) {
            /*imgSharePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onShareClick(producto, imgMain);
                }
            });*/
            imgAddAbono.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAddAbonoClick(producto);
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(producto);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(producto);
                    return false;
                }
            });
        }
    }
}
