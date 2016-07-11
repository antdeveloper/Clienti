package com.artec.mobile.clienti.main.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.addClient.ui.AddClientFragment;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.login.ui.LoginActivity;
import com.artec.mobile.clienti.main.MainPresenter;
import com.artec.mobile.clienti.main.ui.adapters.MainAdapter;
import com.artec.mobile.clienti.main.ui.adapters.OnItemClickListener;
import com.artec.mobile.clienti.productos.ui.ProductosActivity;

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

    @Inject
    MainPresenter presenter;
    @Inject
    MainAdapter adapter;
    @Inject
    SharedPreferences sharedPreferences;

    private ClientiApp app;

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
        switch (item.getItemId()){
            case R.id.action_logout:{
                presenter.logout();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent     );
                break;
            }
        }
        return super.onOptionsItemSelected(item);
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
    protected void onPause() {
        super.onPause();
        presenter.onPause();
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
    public void onItemClick(Client client) {
        Intent intent = new Intent(this, ProductosActivity.class);
        intent.putExtra(ProductosActivity.USERNAME_KEY, client.getUsername());
        intent.putExtra(ProductosActivity.EMAIL_KEY, client.getEmail());
        intent.putExtra(ProductosActivity.ADEUDO_KEY, client.getAdeudo());
        intent.putExtra(ProductosActivity.PAGADO_KEY, client.getPagado());
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    public void addContact(){
        new AddClientFragment().show(getSupportFragmentManager(),
                getString(R.string.addclient_message_title));
    }
}
