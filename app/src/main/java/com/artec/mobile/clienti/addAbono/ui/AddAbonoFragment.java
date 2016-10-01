package com.artec.mobile.clienti.addAbono.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.addAbono.AddAbonoPresenter;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.productos.ui.ProductosActivity;

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

    @Bind(R.id.editTxtAbono)
    EditText editTxtAbono;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private ClientiApp app;

    private Producto producto;

    public AddAbonoFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogFragmentTheme)
                .setTitle(R.string.addabono_message_title)
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

        producto = ((ProductosActivity)getActivity()).productoSelected;

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
                    Abono abono = new Abono();
                    abono.setValor(Double.valueOf(editTxtAbono.getText().toString()));
                    abono.setFecha(System.currentTimeMillis());

                    presenter.addAbono(producto, abono,
                            ((ProductosActivity)getActivity()).client);
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    private void setupInjection() {
        app.getAddContactComponent(this, this).inject(this);
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
        Toast.makeText(getActivity(), R.string.addabono_message_abobboadded,
                Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void abonoNotAdded() {
        producto.setAbono(producto.getAbono() - Double.valueOf(editTxtAbono.getText().toString()));
        editTxtAbono.setText("");
        editTxtAbono.setError(getString(R.string.addclient_error_add));
    }
}
