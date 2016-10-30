package com.artec.mobile.clienti.main.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.addClient.ui.AddClientFragment;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.login.ui.LoginActivity;
import com.artec.mobile.clienti.main.MainPresenter;
import com.artec.mobile.clienti.main.ui.adapters.MainAdapter;
import com.artec.mobile.clienti.main.ui.adapters.OnItemClickListener;
import com.artec.mobile.clienti.productos.ui.ProductosActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainView, OnItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerViewClient)
    RecyclerView recyclerViewClient;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    MainPresenter presenter;
    @Inject
    MainAdapter adapter;
    @Inject
    SharedPreferences sharedPreferences;

    private ClientiApp app;

    private Client mClient;
    private int indexClient = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        app = (ClientiApp) getApplication();
        setupInjection();
        setupRecyclerView();

        presenter.onCreate();
        setupToolbar();
    }

    private void setupRecyclerView() {
        recyclerViewClient.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClient.setAdapter(adapter);
    }

    private void setupToolbar() {
        String email = sharedPreferences.getString(app.getUserName(), getString(R.string.app_name));
        toolbar.setTitle(email);
        setSupportActionBar(toolbar);
    }

    private void setupInjection() {
        app.getMainComponent(this, this, this).inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout: {
                presenter.logout();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
            case R.id.action_about: {
                goToAboutHanlder();
                break;
            }
            case R.id.action_show_adeudos: {
                getAdeudo();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAdeudo() {
        if (adapter != null && adapter.getItemCount()>0) {
            mClient = adapter.getClientList().get(indexClient);
            presenter.onGetAdeudo(mClient.getEmail());
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
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
    public void onClientAdded(Client client) {
        adapter.add(client);
    }

    @Override
    public void onClientChanged(Client client) {
        adapter.update(client);
    }

    @Override
    public void onGetProducts(HashMap<String, ArrayList> productos) {
        mClient.setProductos(productos);
        adapter.update(mClient);
        indexClient++;
        if (indexClient < adapter.getItemCount()) {
            getAdeudo();
        } else {
            indexClient = 0;
            refreshAdeudo();
        }
    }

    private void refreshAdeudo() {
        String username = sharedPreferences.getString(app.getUserName(), getString(R.string.app_name));
        toolbar.setTitle(username + " - $" +
                String.format(Locale.ROOT, "%,.2f", adapter.getDeudasTotal()));
    }

    @Override
    public void onItemClick(Client client) {
        Intent intent = new Intent(this, ProductosActivity.class);
        intent.putExtra(ProductosActivity.USERNAME_KEY, client.getUsername());
        intent.putExtra(ProductosActivity.EMAIL_KEY, client.getEmail());
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    public void addContact() {
        new AddClientFragment().show(getSupportFragmentManager(),
                getString(R.string.addclient_message_title));
    }


    public void goToAboutHanlder() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.about, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.menu_about)
                .setView(view)
                .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}
