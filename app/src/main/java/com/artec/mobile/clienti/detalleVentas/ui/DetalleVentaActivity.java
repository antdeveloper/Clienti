package com.artec.mobile.clienti.detalleVentas.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.addAbono.ui.AddAbonoFragment;
import com.artec.mobile.clienti.admonAbono.ui.AdmonAbonoFragment;
import com.artec.mobile.clienti.admonAbono.utils.AdmonAbonoAux;
import com.artec.mobile.clienti.detalleVentas.DetalleVentaPresenter;
import com.artec.mobile.clienti.detalleVentas.ui.adapters.OnAbonoClickListener;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.Constants;
import com.artec.mobile.clienti.libs.base.ImageLoader;
import com.google.gson.Gson;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetalleVentaActivity extends AppCompatActivity implements DetalleVentaView,
        OnAbonoClickListener, AdmonAbonoAux {
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
    @Bind(R.id.imgbtnAddAbono)
    ImageButton imgbtnAddAbono;
    @Bind(R.id.contentOptions)
    LinearLayout contentOptions;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.imgMain)
    ImageView imgMain;
    @Bind(R.id.txtFechaVenta)
    TextView txtFechaVenta;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.container)
    NestedScrollView container;
    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.tilName)
    TextInputLayout tilName;
    @Bind(R.id.etModel)
    EditText etModel;
    @Bind(R.id.tilModel)
    TextInputLayout tilModel;
    @Bind(R.id.etPrecioOriginal)
    EditText etPrecioOriginal;
    @Bind(R.id.tilPrecioOriginal)
    TextInputLayout tilPrecioOriginal;
    @Bind(R.id.etPrecio)
    EditText etPrecio;
    @Bind(R.id.tilPrecio)
    TextInputLayout tilPrecio;
    @Bind(R.id.etNotas)
    EditText etNotas;
    @Bind(R.id.tilNotas)
    TextInputLayout tilNotas;
    @Bind(R.id.imgPhotoProduct)
    ImageView imgPhotoProduct;
    @Bind(R.id.imgTakeFoto)
    ImageView imgTakeFoto;
    @Bind(R.id.imgDeleteFoto)
    ImageView imgDeleteFoto;
    @Bind(R.id.containerDetails)
    RelativeLayout containerDetails;
    @Bind(R.id.iEditProduct)
    RelativeLayout containerEdit;
    @Bind(R.id.contentMain)
    RelativeLayout contentMain;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    DetalleVentaPresenter presenter;
    @Inject
    ImageLoader imageLoader;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    private ClientiApp app;

    private MenuItem menuItem;
    private AbonoHistoryFragmentListener fragmentListener;
    private Producto mProducto;
    public Client client;
    public Producto productoSelected;
    private Abono abonoSelect;
    private int mode;
    public boolean isAbonoGral;
    public boolean isFromDetalle;
    private String clientEmail;

    private boolean isDetailMode = true;
    private boolean isAnimationInProccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_venta);
        ButterKnife.bind(this);

        app = (ClientiApp) getApplication();
        setupInjection();

        mProducto = new Gson().fromJson(getIntent().getStringExtra(Constants.OBJ_PRODUCTO), Producto.class);
        clientEmail = getIntent().getStringExtra(Constants.EMAIL);
        client = new Client();
        client.setEmail(clientEmail);

        setupFragment();
        setupActionBar();
        setupAbonos();
        setupProduct();
        setupImageProduct();

        presenter.onCreate();
    }

    private void setupInjection() {
        app.getDetalleAbonoComponent(this, this).inject(this);
    }

    private void setupFragment() {
        AbonoHistoryFragment fragment = (AbonoHistoryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentList);
        fragment.setRetainInstance(true);
        fragmentListener = fragment;
        fragmentListener.setListenerFragment(this);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mProducto.getName());
        }
    }

    private void setupAbonos() {
        if (mProducto.getAbonos() != null && mProducto.getAbonos().size() > 0) {
            for (int i = 0; i < mProducto.getAbonos().size(); i++) {
                fragmentListener.addAbono((Abono) mProducto.getAbonosSorted().values().toArray()[i]);
            }
        }
    }

    private void setupProduct() {
        double total = mProducto.getPrecio() * mProducto.getCantidad();
        double abono = fragmentListener.getAbonos();
        txtModel.setText(getString(R.string.productos_hint_model) + ": " +
                mProducto.getModelo());
        txtPrecioProveedor.setText(String.format(Locale.ROOT, getString(R.string.ventas_property_precioOriginal),
                mProducto.getPrecioOriginal()));
        txtPrecio.setText(String.format(Locale.ROOT, getString(R.string.ventas_property_precioVenta),
                mProducto.getPrecio()));
        txtTotal.setText(String.format(Locale.ROOT, getString(R.string.ventas_detalle_property_total), total));
        txtAbono.setText(String.format(Locale.ROOT, getString(R.string.ventas_detalle_property_pagado), abono));
        txtAdeudo.setText(String.format(Locale.ROOT, getString(R.string.ventas_property_adeudo),
                total - abono));
        txtCantidad.setText(getString(R.string.ventas_property_cantidad) + ": (x" +
                String.format(Locale.getDefault(), "%d", mProducto.getCantidad()) + ")");
        txtFechaVenta.setText(new SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                .format(mProducto.getFechaVenta()));
        txtGanancia.setText(String.format(Locale.ROOT, getString(R.string.ventas_property_ganancia),
                mProducto.getGanancia()));
        txtObservaciones.setText(getString(R.string.ventas_property_notas) + ": " +
                mProducto.getNotas());

        etName.setText(mProducto.getName());
        etModel.setText(mProducto.getModelo());
        etPrecio.setText(String.format(Locale.ROOT, "%.2f", mProducto.getPrecio()));
        etPrecioOriginal.setText(String.format(Locale.ROOT, "%.2f", mProducto.getPrecioOriginal()));
        etNotas.setText(mProducto.getNotas());
    }

    private void setupImageProduct() {
        if (mProducto.getUrl().isEmpty()) {
            imgMain.setImageResource(R.drawable.ic_file_image);
            imgPhotoProduct.setImageResource(R.drawable.ic_file_image);
        } else {
            imageLoader.load(imgMain, mProducto.getUrl());
            imageLoader.load(imgPhotoProduct, mProducto.getUrl());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_venta, menu);
        menuItem = menu.findItem(R.id.action_save);
        menuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (isDetailMode) {
                    finish();
                } else {
                    backToDetail();
                }
                return true;
            }
            case R.id.action_save: {
                mProducto.setName(etName.getText().toString());
                mProducto.setModelo(etModel.getText().toString());
                mProducto.setPrecioOriginal(Double.valueOf(etPrecioOriginal.getText().toString()));
                mProducto.setPrecio(Double.valueOf(etPrecio.getText().toString()));
                mProducto.setNotas(etNotas.getText().toString());
                presenter.update(mProducto, "", clientEmail);
                return true;
            }
        }

        return false;
    }

    private void backToDetail() {
        if (!isAnimationInProccess) {
            isAnimationInProccess = true;
            appBar.setExpanded(true);
            container.setNestedScrollingEnabled(true);
            menuItem.setVisible(false);
            isDetailMode = true;
            showDetail();
        }
    }

    @OnClick(R.id.fab)
    public void collapseExpand() {
        if (!isAnimationInProccess) {
            isAnimationInProccess = true;
            appBar.setExpanded(false);
            container.setNestedScrollingEnabled(false);
            menuItem.setVisible(true);
            isDetailMode = false;
            showEdit();
        }
    }

    private void showEdit() {
        TransitionSet set = new TransitionSet()
                .addTransition(new Scale(0.7f))
                .addTransition(new Fade())
                .setInterpolator(isDetailMode ?
                        new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                .addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {
                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {
                        TransitionManager.beginDelayedTransition(contentMain, transition);
                        containerEdit.setVisibility(isDetailMode ? View.INVISIBLE : View.VISIBLE);
                        TransitionManager.endTransitions(contentMain);
                        isAnimationInProccess = false;
                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {
                    }

                    @Override
                    public void onTransitionPause(Transition transition) {
                    }

                    @Override
                    public void onTransitionResume(Transition transition) {
                    }
                });
        TransitionManager.beginDelayedTransition(contentMain, set);
        containerDetails.setVisibility(isDetailMode ? View.VISIBLE : View.INVISIBLE);
    }

    private void showDetail() {
        TransitionSet transitionSet = new TransitionSet()
                .addTransition(new Scale(0.7f))
                .addTransition(new Fade())
                .setInterpolator(isDetailMode ?
                        new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                .addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {
                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {
                        TransitionManager.beginDelayedTransition(contentMain, transition);
                        containerDetails.setVisibility(isDetailMode ? View.VISIBLE : View.INVISIBLE);
                        TransitionManager.endTransitions(contentMain);
                        isAnimationInProccess = false;
                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {
                    }

                    @Override
                    public void onTransitionPause(Transition transition) {
                    }

                    @Override
                    public void onTransitionResume(Transition transition) {
                    }
                });
        TransitionManager.beginDelayedTransition(contentMain, transitionSet);
        containerEdit.setVisibility(isDetailMode ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void enableUIElements() {
        setInputs(true);
    }

    @Override
    public void disableUIElements() {
        setInputs(false);
    }

    @Override
    public void productInit() {
        showSnackbar(R.string.productos_notice_upload_init);
    }

    @Override
    public void productUpdate() {
        setupProduct();
        toolbarLayout.setTitle(mProducto.getName());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mProducto.getName());
        }
        backToDetail();
        showSnackbar(R.string.productos_notice_upload_complete);
    }

    @Override
    public void addAbono(Abono abono) {
        fragmentListener.addAbono(abono);
        /*if (mProducto.getAbonos() == null){
            mProducto.setAbonos(new HashMap<String, Abono>());
        }*/

        //mProducto.getAbonos().put(abono.getDateFormatted(), abono);
        setupProduct();
    }

    @Override
    public void removeAbono(Abono abono) {
        fragmentListener.deleteAbono(abono);
        setupProduct();
    }

    @Override
    public void updateAbono(Abono abono) {
        fragmentListener.updateAbono(abono);
        setupProduct();
    }

    @Override
    public void onError(String error) {
        showSnackbar(error);
    }

    @Override
    public void OnItemLongClick(Abono abono) {
        mode = AdmonAbonoFragment.MODE_EDIT;
        abonoSelect = abono;
        new AdmonAbonoFragment().show(getSupportFragmentManager(), getString(R.string.admonAbono_message_titleEdit));
    }

    private void setInputs(boolean enabled) {
        etName.setEnabled(enabled);
        etModel.setEnabled(enabled);
        etPrecioOriginal.setEnabled(enabled);
        etPrecio.setEnabled(enabled);
        etNotas.setEnabled(enabled);
    }

    private void showSnackbar(String msg) {
        Snackbar.make(contentMain, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void showSnackbar(int strResource) {
        Snackbar.make(contentMain, strResource, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.imgbtnAddAbono)
    public void addAbonoHandler() {
        /*isAbonoGral = false;
        productoSelected = mProducto;
        isFromDetalle = true;
        new AddAbonoFragment().show(getSupportFragmentManager(), getString(R.string.addabono_message_title));*/
        mode = AdmonAbonoFragment.MODE_ADD;
        new AdmonAbonoFragment().show(getSupportFragmentManager(), getString(R.string.admonAbono_message_title));
    }

    /******************
     * Metodos auxiliares para admonAdeudosFragment
     * ****************/
    @Override
    public int getMode() {
        return mode;
    }

    @Override
    public void abonoAdded(Abono abono) {
        addAbono(abono);
    }

    @Override
    public void abonoUpdated(Abono abono) {
        updateAbono(abono);
    }

    @Override
    public void abonoDeleted(Abono abono) {
        removeAbono(abono);
    }

    @Override
    public Abono getAbono() {
        return abonoSelect;
    }

    @Override
    public Producto getProducto() {
        return mProducto;
    }

    @Override
    public List<Producto> getProductos() {
        return null;
    }

    @Override
    public Client getClient() {
        return client;
    }
}
