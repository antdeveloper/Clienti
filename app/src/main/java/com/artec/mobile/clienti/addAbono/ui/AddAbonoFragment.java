package com.artec.mobile.clienti.addAbono.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.addAbono.AddAbonoPresenter;
import com.artec.mobile.clienti.detalleVentas.ui.DetalleVentaActivity;
import com.artec.mobile.clienti.detalleVentas.ui.DetalleVentaView;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.productos.ui.ProductosActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AddAbonoFragment extends DialogFragment implements AddAbonoView,
        DialogInterface.OnShowListener {

    @Inject
    AddAbonoPresenter presenter;

    @Bind(R.id.etAbono)
    EditText editTxtAbono;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private ClientiApp app;

    private Abono abono;
    public Client client;
    private Producto producto;
    private List<Producto> productos;
    private boolean isAbonoGral;
    private boolean isFromDetalle;

    public AddAbonoFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            isFromDetalle = ((DetalleVentaActivity)getActivity()).isFromDetalle;
        }catch (Exception e){
            isFromDetalle = ((ProductosActivity)getActivity()).isFromDetalle;
        }

        if (isFromDetalle){
            isAbonoGral = ((DetalleVentaActivity) getActivity()).isAbonoGral;
            client = ((DetalleVentaActivity) getActivity()).client;
        } else {
            isAbonoGral = ((ProductosActivity) getActivity()).isAbonoGral;
            client = ((ProductosActivity) getActivity()).client;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogFragmentTheme)
                .setTitle(isAbonoGral?R.string.addabono_message_titleGral : R.string.addabono_message_title)
                .setPositiveButton(R.string.addclient_message_acept,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setNegativeButton(R.string.addclient_message_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add_abono, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);

        app = (ClientiApp) getActivity().getApplication();
        setupInjection();
        presenter.onShow();


        if (isFromDetalle){
            producto = ((DetalleVentaActivity) getActivity()).productoSelected;
        } else {
            if (isAbonoGral){
                productos = ((ProductosActivity)getActivity()).getProductos();
            }else {
                producto = ((ProductosActivity) getActivity()).productoSelected;
            }
        }
        return dialog;
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double valorAbono = Double.valueOf(editTxtAbono.getText().toString().isEmpty()?
                            "0" : editTxtAbono.getText().toString());
                    if (valorAbono == 0){
                        editTxtAbono.setText("");
                        editTxtAbono.setError(getString(R.string.productos_error_required));
                    } else {
                        if (isAbonoGral){
                            double deudaTotal = getAdeudoTotal();
                            if (valorAbono > deudaTotal){
                                editTxtAbono.setError(getString(R.string.addAbono_error_abonoBiger)+
                                        " ($"+deudaTotal+")");
                            }else {
                                presenter.addAbonoGral(productos, valorAbono, System.currentTimeMillis(), client);
                            }
                        }else {
                            if (valorAbono > producto.getAdeudo()){
                                editTxtAbono.setError(getString(R.string.addAbono_error_abonoBiger)+
                                        " ($"+producto.getAdeudo()+")");
                            }else {
                                abono = new Abono();
                                abono.setValor(valorAbono);
                                abono.setFecha(System.currentTimeMillis());

                                presenter.addAbono(producto, abono, client);
                            }
                        }
                    }
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTxtAbono, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void setupInjection() {
        app.getAddAbonoComponent(this, this).inject(this);
    }

    @Override
    public void showInput() {
        editTxtAbono.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInput() {
        editTxtAbono.setVisibility(View.GONE);
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
    public void abonoAdded() {
        Toast.makeText(getActivity(), R.string.addAbono_message_abonoAdded,
                Toast.LENGTH_SHORT).show();
        if (isFromDetalle){
            ((DetalleVentaView)getActivity()).addAbono(abono);
        }
        dismiss();
    }

    @Override
    public void abonoNotAdded() {
        producto.setAbono(producto.getAbono() - Double.valueOf(editTxtAbono.getText().toString()));
        editTxtAbono.setText("");
        editTxtAbono.setError(getString(R.string.addclient_error_add));
    }

    public double getAdeudoTotal(){
        double adeudo = 0;
        for (Producto producto : productos){
            adeudo += producto.getAdeudo();
        }
        return adeudo;
    }
}
