package com.artec.mobile.clienti.detalleVentas.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.Constants;
import com.artec.mobile.clienti.libs.base.ImageLoader;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetalleVentaActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.txtModel)
    TextView txtModel;
    @Bind(R.id.txtPrecioProveedor)
    TextView txtPrecioProveedor;
    @Bind(R.id.txtPrecio)
    TextView txtPrecio;
    @Bind(R.id.txtTotal)
    TextView txtTotal;
    @Bind(R.id.txtAbono)
    TextView txtAbono;
    @Bind(R.id.txtAdeudo)
    TextView txtAdeudo;
    @Bind(R.id.txtGanancia)
    TextView txtGanancia;
    @Bind(R.id.txtObservaciones)
    TextView txtObservaciones;
    @Bind(R.id.txtCantidad)
    TextView txtCantidad;
    @Bind(R.id.imgSharePhoto)
    ImageButton imgSharePhoto;
    @Bind(R.id.contentOptions)
    LinearLayout contentOptions;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.imgMain)
    ImageView imgMain;
    @Bind(R.id.txtFechaVenta)
    TextView txtFechaVenta;

    private AbonoHistoryFragmentListener fragmentListener;
    private Producto mProducto;
    @Inject
    ImageLoader imageLoader;

    private ClientiApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_venta);
        ButterKnife.bind(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Proximamente...", Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();
            }
        });

        app = (ClientiApp)getApplication();
        setupInjection();

        mProducto = new Gson().fromJson(getIntent().getStringExtra(Constants.OBJ_PRODUCTO), Producto.class);
        setupFragment();
        setupActionBar();
        setupProduct();
        setupAbonos();
    }

    private void setupInjection() {
        app.getDetalleAbonoComponent(this).inject(this);
    }

    private void setupFragment() {
        AbonoHistoryFragment fragment = (AbonoHistoryFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragmentList);
        fragment.setRetainInstance(true);
        fragmentListener = fragment;
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mProducto.getName());
        }
    }

    private void setupProduct() {
        double total = mProducto.getPrecio() * mProducto.getCantidad();
        if (mProducto.getUrl().isEmpty()) {
            imgMain.setImageResource(R.drawable.ic_file_image);
        } else {
            imageLoader.load(imgMain, mProducto.getUrl());
        }
        txtModel.setText(getString(R.string.productos_hint_model) + ": " +
                mProducto.getModelo());
        txtPrecioProveedor.setText(String.format(Locale.getDefault(), getString(R.string.ventas_property_precioOriginal),
                mProducto.getPrecioOriginal()));
        txtPrecio.setText(String.format(Locale.getDefault(), getString(R.string.ventas_property_precioVenta),
                mProducto.getPrecio()));
        txtTotal.setText(String.format(Locale.getDefault(), getString(R.string.ventas_detalle_property_total), total));
        txtAbono.setText(String.format(Locale.getDefault(), getString(R.string.ventas_detalle_property_pagado),
                mProducto.getAbono()));
        txtAdeudo.setText(String.format(Locale.getDefault(), getString(R.string.ventas_property_adeudo),
                total - mProducto.getAbono()));
        txtCantidad.setText(getString(R.string.ventas_property_cantidad) + ": (x" +
                String.format(Locale.getDefault(), "%d", mProducto.getCantidad()) + ")");
        txtFechaVenta.setText(new SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                .format(mProducto.getFechaVenta()));
        txtGanancia.setText(String.format(Locale.getDefault(), getString(R.string.ventas_property_ganancia),
                mProducto.getGanancia()));
        txtObservaciones.setText(getString(R.string.ventas_property_notas) + ": " +
                mProducto.getNotas());
    }

    private void setupAbonos(){
        if (mProducto.getAbonos() != null && mProducto.getAbonos().size() > 0) {
            for (int i=0; i<mProducto.getAbonos().size(); i++){
                fragmentListener.addAbono((Abono) mProducto.getAbonosSorted().values().toArray()[i]);
            }
        }
        /*if (mProducto.getAbonos() != null && mProducto.getAbonos().size() > 0) {
            for (int i=0; i<mProducto.getAbonos().size(); i++){
                fragmentListener.addAbono((Abono) mProducto.getAbonos().values().toArray()[i]);
            }
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }

        return false;
    }
}
