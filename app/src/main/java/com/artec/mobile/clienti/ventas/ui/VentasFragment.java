package com.artec.mobile.clienti.ventas.ui;


import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.addAbono.ui.AddAbonoFragment;
import com.artec.mobile.clienti.detalleVentas.ui.DetalleVentaActivity;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.libs.Constants;
import com.artec.mobile.clienti.libs.Utilities;
import com.artec.mobile.clienti.productos.ui.ProductosActivity;
import com.artec.mobile.clienti.ventas.VentasPresenter;
import com.artec.mobile.clienti.ventas.ui.adapters.OnItemClickListener;
import com.artec.mobile.clienti.ventas.ui.adapters.VentasAdapter;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VentasFragment extends Fragment implements VentasView,
        OnItemClickListener {

    private static final int PERMISSIONS_REQUEST_STORAGE = 1;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.container)
    FrameLayout container;

    @Inject
    VentasAdapter adapter;
    @Inject
    VentasPresenter presenter;

    private ImageView imgProduct;

    public VentasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInjection();
        presenter.onCreate();
    }

    private void setupInjection() {
        ClientiApp app = (ClientiApp) getActivity().getApplication();
        app.getPhotoListComponent(this, this, this).inject(this);
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content_productos, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView();
        presenter.subscribe(((ProductosActivity)getActivity()).client.getEmail());

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.columns)));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showList() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideList() {
        recyclerView.setVisibility(View.GONE);
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
    public void addProducto(Producto producto) {
        adapter.addPhoto(producto);
        refreshAdeudo();
    }

    @Override
    public void updateProducto(Producto producto) {
        adapter.update(producto);
        refreshAdeudo();
    }

    @Override
    public void removeProducto(Producto producto) {
        adapter.removePhoto(producto);
        refreshAdeudo();
    }

    @Override
    public void onProductoError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Producto producto) {
        Intent intent = new Intent(getActivity(), DetalleVentaActivity.class);
        intent.putExtra(Constants.OBJ_PRODUCTO, new Gson().toJson(producto));
        intent.putExtra(Constants.EMAIL, ((ProductosActivity)getActivity()).client.getEmail());
        if (Utilities.materialDesign()){
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(final Producto producto) {
        if (producto.isPublishByMe()) {
            Vibrator v = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(60);
            new AlertDialog.Builder(getActivity(), R.style.DialogFragmentTheme)
                    .setTitle(getString(R.string.ventas_title_confirmdelete))
                    .setMessage(getString(R.string.ventas_message_confirmdelete))
                    .setPositiveButton(getString(R.string.addclient_message_acept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onDeleteClick(producto);
                        }
                    })
                    .setNegativeButton(getString(R.string.addclient_message_cancel), null)
                    .show();
        }
    }

    @Override
    public void onShareClick(Producto producto, ImageView img) {
        imgProduct = img;
        checkPermissionStorage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (imgProduct != null) {
                shareImage();
            }
        }
    }

    private void checkPermissionStorage() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_STORAGE);
            }
            return;
        }
        shareImage();
    }

    private void shareImage(){
        //Bitmap bitmap = ((GlideBitmapDrawable)img.getDrawable()).getBitmap();
        imgProduct.buildDrawingCache();
        Bitmap bitmap = imgProduct.getDrawingCache();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null, null);
        Uri imageUri = Uri.parse(path);

        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(share, getString(R.string.ventas_message_share)));
    }

    private void refreshAdeudo(){
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            if (getResources().getBoolean(R.bool.isTablet)){
                actionBar.setTitle(((ProductosActivity) getActivity()).client.getUsername() + " - $" +
                        String.format(Locale.ROOT, "%,.2f", adapter.getAdeudoTotal()));
            } else {
                actionBar.setSubtitle("$" + String.format(Locale.ROOT, "%,.2f", adapter.getAdeudoTotal()));
            }
        }
        ((ProductosActivity)getActivity()).setProductos(adapter.getProductos());
    }

    @Override
    public void onDeleteClick(Producto producto) {
        presenter.removePhoto(producto, ((ProductosActivity)getActivity()).client);
    }

    @Override
    public void onAddAbonoClick(Producto producto) {
        setupProductoForAbono(producto);
        new AddAbonoFragment().show(getActivity().getSupportFragmentManager(),
                getString(R.string.addabono_message_title));
    }

    private void setupProductoForAbono(Producto producto) {
        ((ProductosActivity)getActivity()).isAbonoGral = false;
        ((ProductosActivity)getActivity()).isFromDetalle = false;
        ((ProductosActivity)getActivity()).productoSelected = producto;
    }
}
