package com.artec.mobile.clienti.productos.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.compras.ui.ComprasFragment;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.productos.ProductosPresenter;
import com.artec.mobile.clienti.productos.ui.adapters.ProductosSectionPageAdapter;
import com.artec.mobile.clienti.ventas.ui.VentasFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductosActivity extends AppCompatActivity implements ProductosView {

    public static final String EMAIL_KEY = "email";
    public static final String ADEUDO_KEY = "adeudo";
    public static final String PAGADO_KEY = "pagado";
    public static final String USERNAME_KEY = "username";

    private final static int REQUEST_PICTURE = 0;
    private static final int PERMISSIONS_REQUEST_STORAGE = 2;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Inject
    ProductosPresenter presenter;
    @Inject
    ProductosSectionPageAdapter adapter;
    @Inject
    SharedPreferences sharedPreferences;

    private AlertDialog alertDialog;
    private Uri uriFoto;

    public Client client;
    private String photoPath;
    private ClientiApp app;

    public Producto productoSelected;

    private boolean isSaving = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        ButterKnife.bind(this);

        client = new Client();
        client.setEmail(getIntent().getStringExtra(EMAIL_KEY));
        client.setUsername(getIntent().getStringExtra(USERNAME_KEY));
        client.setAdeudo(getIntent().getDoubleExtra(ADEUDO_KEY, 0));
        client.setPagado(getIntent().getDoubleExtra(PAGADO_KEY, 0));

        app = (ClientiApp) getApplication();
        setupInjection();
        setupNavigation();

        presenter.onCreate();
    }

    private void setupNavigation() {
        toolbar.setTitle(client.getUsername());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }

    private void setupInjection() {
        String[] titles = new String[]{getString(R.string.productos_title_ventas), getString(R.string.productos_title_compras)};

        Fragment[] fragments = new Fragment[]{new VentasFragment(), new ComprasFragment()};

        app.getVentasMainComponent(this, getSupportFragmentManager(), fragments, titles).inject(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PICTURE: {
                if (resultCode == RESULT_OK) {
                    boolean fromCamera = (data == null || data.getData() == null);
                    String uriStr = "";
                    if (fromCamera) {
                        addToGallery();
                        uriStr = uriFoto.toString();
                    } else {
                        photoPath = getRealPathFromURI(data.getData());
                        uriStr = data.getDataString();
                    }
                    ImageView imgProduct = (ImageView)(alertDialog).findViewById(R.id.imgPhotoProduct);
                    imgProduct.setImageBitmap(reduceBitmap(ProductosActivity.this, uriStr, 320, 320));
                }
                break;
            }
        }
    }

    public static Bitmap reduceBitmap(Context context, String uri, int maxAncho, int maxAlto){
        try{
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.parse(uri)),
                    null, options);
            options.inSampleSize = (int)Math.max(
                    Math.ceil(options.outWidth / maxAncho),
                    Math.ceil(options.outHeight / maxAlto));
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(Uri.parse(uri)), null, options);
        }catch (FileNotFoundException e){
            Toast.makeText(context, R.string.productos_error_notfound, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onUploadInit() {
        showSnackbar(R.string.productos_notice_upload_init);
    }

    @Override
    public void onUploadComplete() {
        alertDialog.dismiss();
        photoPath = "";
        showSnackbar(R.string.productos_notice_upload_complete);
        isSaving = false;
    }

    @Override
    public void onUploadError(String error) {
        showSnackbar(error);
        isSaving = false;
        ProgressBar progressBar = (ProgressBar)(alertDialog).findViewById(R.id.progressBar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAbonoAdded(Abono abono) {
        //this.client = abono;
    }

    @OnClick(R.id.fab)
    public void createVentaHandler() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_producto, null);

        final EditText etName = (EditText)view.findViewById(R.id.etName);
        final EditText etModel = (EditText)view.findViewById(R.id.etModel);
        final EditText etCantidad = (EditText)view.findViewById(R.id.etCantidad);
        final EditText etPrecio = (EditText)view.findViewById(R.id.etPrecio);
        final EditText etAbono = (EditText)view.findViewById(R.id.etAbono);
        final ImageView imgPhotoProduct = (ImageView)view.findViewById(R.id.imgPhotoProduct);
        ImageView imgTakeFoto = (ImageView)view.findViewById(R.id.imgTakeFoto);
        ImageView imgDeleteFoto = (ImageView)view.findViewById(R.id.imgDeleteFoto);
        final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        imgTakeFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takePicture();
                checkPermissionStorage();
            }
        });

        imgDeleteFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoPath != null && !photoPath.isEmpty()) {
                    imgPhotoProduct.setImageBitmap(null);
                    photoPath = "";
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogFragmentTheme)
                .setTitle(R.string.productos_message_titledialog)
                .setPositiveButton(R.string.addclient_message_acept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton(R.string.addclient_message_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setView(view);

        alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnAceptar = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validateFields() && !isSaving) {
                            isSaving = true;
                            progressBar.setVisibility(View.VISIBLE);
                            Producto producto = new Producto();
                            producto.setName(etName.getText().toString());
                            producto.setModelo(etModel.getText().toString());
                            producto.setCantidad(Integer.valueOf(etCantidad.getText().toString()));
                            producto.setPrecio(Double.valueOf(etPrecio.getText().toString()));
                            producto.setFechaVenta(System.currentTimeMillis());
                            /*producto.setAbono(etAbono.getText().toString().isEmpty()? 0 :
                                    Double.valueOf(etAbono.getText().toString()));*/
                            //presenter.uploadPhoto(producto, photoPath, client);
                            Abono abono = new Abono();
                            abono.setValor(etAbono.getText().toString().isEmpty()? 0 :
                                    Double.valueOf(etAbono.getText().toString()));
                            abono.setFecha(System.currentTimeMillis());
                            presenter.uploadPhoto(producto, abono, saveImageLocally(), client);
                        }
                    }

                    private boolean validateFields() {
                        if (etName.getText().toString().isEmpty()){
                            etName.setError(getString(R.string.productos_error_required));
                            return false;
                        }
                        if (etModel.getText().toString().isEmpty()){
                            etModel.setError(getString(R.string.productos_error_required));
                            return false;
                        }
                        if (etCantidad.getText().toString().isEmpty()){
                            etCantidad.setError(getString(R.string.productos_error_required));
                            return false;
                        }
                        if (etPrecio.getText().toString().isEmpty()){
                            etPrecio.setError(getString(R.string.productos_error_required));
                            return false;
                        }
                        /*if (photoPath == null || photoPath.isEmpty()){
                            Toast.makeText(ProductosActivity.this, R.string.productos_error_notphotoselect,
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        }*/

                        return true;
                    }

                    private String saveImageLocally() {
                        if (photoPath == null || photoPath.isEmpty()){
                            return "";
                        }

                        imgPhotoProduct.buildDrawingCache();
                        Bitmap _bitmap = imgPhotoProduct.getDrawingCache();

                        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        File outputFile = null;
                        try {
                            outputFile = File.createTempFile("tmp", ".jpg", storageDir);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            FileOutputStream out = new FileOutputStream(outputFile);
                            _bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return outputFile.getAbsolutePath();
                    }
                });
                Button btnCancelar = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isSaving){
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_STORAGE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    takePicture();
                }
            }
        }

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void checkPermissionStorage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_STORAGE);
            }
            return;
        }
        takePicture();
    }

    public void takePicture() {
        Intent chooserIntent = null;

        List<Intent> intentsList = new ArrayList<>();
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("return-data", true);

        File photoFile = getFile();
        if (photoFile != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                intentsList = addIntentsToList(intentsList, cameraIntent);
            }
        }

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            intentsList = addIntentsToList(intentsList, pickIntent);
        }

        if (intentsList.size() > 0) {
            chooserIntent = Intent.createChooser(intentsList.remove(intentsList.size() - 1),
                    getString(R.string.productos_message_picture_source));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentsList.toArray(new Parcelable[]{}));
        }

        if (chooserIntent != null) {
            startActivityForResult(chooserIntent, REQUEST_PICTURE);
        }
    }

    private File getFile() {
        File photoFile = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            photoPath = photoFile.getAbsolutePath();
        } catch (IOException e) {
            showSnackbar(R.string.productos_error_dispatch_camera);
        }
        return photoFile;
    }

    private List<Intent> addIntentsToList(List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetIntent = new Intent(intent);
            targetIntent.setPackage(packageName);
            list.add(targetIntent);
        }

        return list;
    }

    private void addToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(photoPath);
        Uri contentUri = Uri.fromFile(file);
        uriFoto = contentUri;
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = null;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            if (contentURI.toString().contains("mediakey")) {
                cursor.close();

                try {
                    File file = File.createTempFile("tempImg", ".jpg", getCacheDir());
                    InputStream input = getContentResolver().openInputStream(contentURI);
                    OutputStream output = new FileOutputStream(file);

                    try {
                        byte[] buffer = new byte[4 * 1024];
                        int read;

                        while ((read = input.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                        result = file.getAbsolutePath();
                    } finally {
                        output.close();
                        input.close();
                    }
                } catch (Exception e) {

                }
            } else {
                cursor.moveToFirst();
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(dataColumn);
                cursor.close();
            }
        }
        return result;
    }

    private void showSnackbar(String msg) {
        Snackbar.make(viewPager, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void showSnackbar(int strResource) {
        Snackbar.make(viewPager, strResource, Snackbar.LENGTH_SHORT).show();
    }
}
