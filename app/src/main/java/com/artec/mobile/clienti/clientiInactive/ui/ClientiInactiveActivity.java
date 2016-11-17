package com.artec.mobile.clienti.clientiInactive.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.clientiInactive.ClientiInactivePresenter;
import com.artec.mobile.clienti.clientiInactive.events.ClientiInactiveEvent;
import com.artec.mobile.clienti.clientiInactive.ui.adapters.ClientiInactiveAdapter;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.libs.Constants;
import com.artec.mobile.clienti.main.ui.adapters.OnItemClickListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClientiInactiveActivity extends AppCompatActivity implements ClientiInactiveView, OnItemClickListener{

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.container)
    CoordinatorLayout container;

    @Inject
    ClientiInactivePresenter presenter;
    @Inject
    ClientiInactiveAdapter adapter;
    @Inject
    SharedPreferences sharedPreferences;

    private ClientiApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clienti_inactive);
        ButterKnife.bind(this);

        app = (ClientiApp)getApplication();

        setupInjection();
        setupRecyclerView();
        setupToolbar();

        presenter.onCreate();
        presenter.onGetClients();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupInjection() {
        app.getClientiInactiveComponent(this, this, this).inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inactives, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
            case R.id.action_reload:{
                presenter.onGetClients();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //presenter.onGetClients();
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
    public void onGetClients(List<Client> clients) {
        adapter.setData(clients);
    }

    @Override
    public void onClientDeleted(String email) {
        adapter.remove(email);
        showSnackbar(R.string.clientiInactive_message_reactived_successfully);
    }

    @Override
    public void onError(int type, String error) {
        switch (type){
            case ClientiInactiveEvent.ERROR:
                showSnackbar(error);
                break;
            case ClientiInactiveEvent.READ:{
                showSnackbar(R.string.message_list_notFound);
                break;
            }
        }
    }

    @Override
    public void onItemClick(final Client client) {
        new AlertDialog.Builder(this, R.style.DialogFragmentTheme)
                .setTitle(getString(R.string.clientiInactive_title_confirmReactive))
                .setMessage(getString(R.string.clientiInactive_message_confirmReactive))
                .setPositiveButton(getString(R.string.addclient_message_acept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //client.setDown(false);
                        client.setEstatus(Constants.ESTATUS_ACTIVO);
                        presenter.onReactiveClient(client);
                    }
                })
                .setNegativeButton(getString(R.string.addclient_message_cancel), null)
                .show();
    }

    @Override
    public void onItemLongClick(final Client client) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(60);
        /*new AlertDialog.Builder(this, R.style.DialogFragmentTheme)
                .setTitle(getString(R.string.clientiInactive_title_confirmDelete))
                .setMessage(getString(R.string.clientiInactive_message_confirmDelete))
                .setPositiveButton(getString(R.string.addclient_message_acept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onDeleteClient(client);
                    }
                })
                .setNegativeButton(getString(R.string.addclient_message_cancel), null)
                .show();*/
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
}
