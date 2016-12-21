package com.artec.mobile.clienti.indicadores.ui;

import android.content.res.Configuration;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.indicadores.IndicadoresPresenter;
import com.artec.mobile.clienti.indicadores.events.IndicadoresEvent;
import com.artec.mobile.clienti.indicadores.utils.ChartFragmentAux;
import com.artec.mobile.clienti.indicadores.utils.IndicadoresAux;
import com.artec.mobile.clienti.main.ui.adapters.MainAdapter;
import com.artec.mobile.clienti.productos.ui.adapters.ProductosSectionPageAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ANICOLAS on 17/11/2016.
 */

public class IndicadoresActivity extends AppCompatActivity implements IndicadoresView,
        IndicadoresAux {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.container)
    CoordinatorLayout container;

    @Inject
    ProductosSectionPageAdapter adapter;
    @Inject
    IndicadoresPresenter presenter;

    private ClientiApp app;

    private ArrayList<Client> clients;
    private ArrayList<Producto> productos;
    private int indexClient = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicadores);
        ButterKnife.bind(this);

        app = (ClientiApp) getApplication();
        setupInjection();
        setupNavigation();

        this.clients = new ArrayList<>();
        this.productos = new ArrayList<>();
        presenter.onCreate();

        if (MainAdapter.getEmails().size() > 0) {
            getData();
        }
    }

    private void setupNavigation() {
        toolbar.setTitle(R.string.indicadores_title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (!((ChartFragmentAux)adapter.getItem(position)).hasData()) {
                    /*((ChartFragmentAux) adapter.getItem(position)).refreshChart();
                } else {*/
                    ((ChartFragmentAux)adapter.getItem(position)).setupChart();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setupInjection() {
        String[] titles = new String[]{getString(R.string.indicadores_chAbono_title),
                getString(R.string.indicadores_chInversion_title),
                getString(R.string.indicadores_chGanancia_title)};

        Fragment[] fragments = new Fragment[]{new ChartAbonoFragment(), new ChartInversionesFragment(),
                new ChartGananciasFragment()};

        app.getIndicadoresComponent(this, this, getSupportFragmentManager(), fragments, titles).inject(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                super.onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        presenter.onGetProducts(MainAdapter.getEmails().get(indexClient));
        if (indexClient == MainAdapter.getEmails().size()-1){
            progressBar.setProgress(100);
        } else {
            progressBar.setProgress((indexClient + 1) * (100 / MainAdapter.getEmails().size()));
        }
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
    public void onGetProducts(List<Producto> productos) {
        indexClient++;
        if (productos == null) {
            if (indexClient < MainAdapter.getEmails().size()) {
                getData();
            } else {
                indexClient = 0;
                ((ChartFragmentAux)adapter.getItem(viewPager.getCurrentItem())).setupChart();// FIXME: 15/12/2016
            }
            return;
        }

        Client client = new Client();
        client.setProductoList(productos);
        clients.add(client);
        this.productos.addAll(productos);
        if (indexClient < MainAdapter.getEmails().size()) {
            getData();
        } else {
            indexClient = 0;
            ((ChartFragmentAux)adapter.getItem(viewPager.getCurrentItem())).setupChart();// FIXME: 15/12/2016
        }
    }

    @Override
    public void onError(int type, String error) {
        switch (type) {
            case IndicadoresEvent.ERROR:
                showSnackbar(error);
                break;
        }
    }

    private void showSnackbar(String msg) {
        showSnackbar(msg, Snackbar.LENGTH_SHORT);
    }

    private void showSnackbar(int strResource) {
        showSnackbar(getString(strResource), Snackbar.LENGTH_SHORT);
    }

    private void showSnackbar(int strResource, int duration) {
        showSnackbar(getString(strResource), duration);
    }

    private void showSnackbar(String msg, int duration) {
        Snackbar.make(container, msg, duration).show();
    }

    /***
     * Metodos auxiliares para las graficas de los fragments
     * **/
    @Override
    public ArrayList<Client> getClients() {
        return clients;
    }
}
