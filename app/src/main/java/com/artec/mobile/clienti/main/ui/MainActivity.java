package com.artec.mobile.clienti.main.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.addClient.ui.AddClientFragment;
import com.artec.mobile.clienti.clientiInactive.ui.ClientiInactiveActivity;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.indicadores.ui.IndicadoresActivity;
import com.artec.mobile.clienti.libs.Constants;
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
    @Bind(R.id.recyclerView)
    RecyclerView recyclerViewClient;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.container)
    CoordinatorLayout container;

    @Inject
    MainPresenter presenter;
    @Inject
    MainAdapter adapter;
    @Inject
    SharedPreferences sharedPreferences;

    private ClientiApp app;

    private Client mClient;
    private int indexClient = 0;
    private boolean isNewClient = false;

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
        registerForContextMenu(recyclerViewClient);
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
            case R.id.action_indicadores:{
                Intent intent = new Intent(this, IndicadoresActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.action_logout: {
                presenter.logout();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
            case R.id.action_show_users:{
                Intent intent = new Intent(this, ClientiInactiveActivity.class);
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
        if (adapter != null && adapter.getItemCount() > 0) {
            if (indexClient == adapter.getItemCount()-1){
                progressBar.setProgress(100);
            } else {
                progressBar.setProgress((indexClient+1)*(100/adapter.getItemCount()));
            }
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
    protected void onPause() {
        super.onPause();
        presenter.onPause();
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
        if (isNewClient) {
            isNewClient = false;
            recyclerViewClient.smoothScrollToPosition(adapter.getItemCount() - 2);
        }
    }

    @Override
    public void onClientChanged(Client client) {
        //if (client.isDown()) {
        if (client.getEstatus() == Constants.ESTATUS_INACTIVO){
            adapter.remove(client);
            showSnackbar(R.string.main_message_downSuccessfully);
        } else {
            adapter.update(client);
            //showSnackbar(R.string.message_save_successfully);
        }
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

    @Override
    public void onItemLongClick(final Client client) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(60);
        /*new android.support.v7.app.AlertDialog.Builder(this, R.style.DialogFragmentTheme)
                .setTitle(getString(R.string.main_title_confirmDown))
                .setMessage(getString(R.string.main_message_confirmDown))
                .setPositiveButton(getString(R.string.addclient_message_acept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //client.setDown(true);
                        client.setEstatus(Constants.ESTATUS_INACTIVO);
                        presenter.onUpdateClient(client);
                    }
                })
                .setNegativeButton(getString(R.string.addclient_message_cancel), null)
                .show();*/
        openContextMenu(recyclerViewClient);
        mClient = client;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_main_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit: {
                View view = getLayoutInflater().inflate(R.layout.dialog_rename_client, null);
                final EditText etName = (EditText)view.findViewById(R.id.etName);
                etName.setText(mClient.getUsername());
                new AlertDialog.Builder(this, R.style.DialogFragmentTheme)
                        .setTitle(R.string.main_menu_action_edit)
                        .setView(view)
                        .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mClient.setUsername(etName.getText().toString());
                                presenter.onUpdateClient(mClient);
                            }
                        })
                        .setNegativeButton(R.string.addclient_message_cancel, null)
                        .show();
                return true;
            }
            case R.id.action_delete: {
                new AlertDialog.Builder(this, R.style.DialogFragmentTheme)
                        .setTitle(getString(R.string.main_title_confirmDown))
                        .setMessage(getString(R.string.main_message_confirmDown))
                        .setPositiveButton(getString(R.string.addclient_message_acept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mClient.setEstatus(Constants.ESTATUS_INACTIVO);
                                presenter.onUpdateClient(mClient);
                            }
                        })
                        .setNegativeButton(getString(R.string.addclient_message_cancel), null)
                        .show();
                return true;
            }
            default: {
                return super.onContextItemSelected(item);
            }
        }
    }

    @OnClick(R.id.fab)
    public void addContact() {
        new AddClientFragment().show(getSupportFragmentManager(),
                getString(R.string.addclient_message_title));
        isNewClient = true;
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

    private void showSnackbar(String msg) {
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void showSnackbar(int strResource) {
        Snackbar.make(container, strResource, Snackbar.LENGTH_SHORT).show();
    }

    public void setNewClient(boolean newClient) {
        isNewClient = newClient;
    }
}
