package com.artec.mobile.clienti.admonAbono.ui;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.admonAbono.AdmonAbonoPresenter;
import com.artec.mobile.clienti.admonAbono.utils.AdmonAbonoAux;
import com.artec.mobile.clienti.entities.Abono;
import com.artec.mobile.clienti.entities.Client;
import com.artec.mobile.clienti.entities.Producto;
import com.artec.mobile.clienti.utils.DialogoSelectorFecha;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AdmonAbonoFragment extends DialogFragment implements AdmonAbonoView,
        DialogInterface.OnShowListener, DatePickerDialog.OnDateSetListener {

    @Inject
    AdmonAbonoPresenter presenter;

    @Bind(R.id.editTxtAbono)
    EditText editTxtAbono;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.tvFecha)
    TextView tvFecha;

    public static final int MODE_ADD = 0;
    public static final int MODE_EDIT = 1;
    public static final int MODE_GRAL = 2;

    private ClientiApp app;

    private Abono abono;
    public Client client;
    private Producto producto;
    private List<Producto> productos;

    // Variables auxiliares para los 3 modos(add, edit y addGral)
    private AdmonAbonoAux aux;
    private int mode;
    private String title;
    private Calendar mCalendar;

    public AdmonAbonoFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_admon_abono, null);
        ButterKnife.bind(this, view);
        setupMode();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogFragmentTheme)
                .setTitle(title)
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
        if (mode == MODE_EDIT){
            builder.setNeutralButton(R.string.label_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);

        app = (ClientiApp) getActivity().getApplication();
        setupInjection();
        setupDateTextView();
        presenter.onShow();

        return dialog;
    }

    private void setupMode(){
        mode = ((AdmonAbonoAux)getActivity()).getMode();
        client = ((AdmonAbonoAux)getActivity()).getClient();

        switch (mode){
            case MODE_ADD:{
                producto = ((AdmonAbonoAux)getActivity()).getProducto();
                title = getString(R.string.admonAbono_message_title);
                break;
            }
            case MODE_EDIT:{
                abono = ((AdmonAbonoAux)getActivity()).getAbono();
                producto = ((AdmonAbonoAux)getActivity()).getProducto();
                title = getString(R.string.admonAbono_message_titleEdit);
                editTxtAbono.setText(String.format(Locale.ROOT, "%.2f", abono.getValor()));
                break;
            }
            case MODE_GRAL:{
                productos = ((AdmonAbonoAux)getActivity()).getProductos();
                title = getString(R.string.admonAbono_message_titleGral);
                break;
            }
        }
    }

    private void setupDateTextView() {
        mCalendar = Calendar.getInstance();
        if (mode == MODE_EDIT) {
            mCalendar.setTimeInMillis(abono.getFecha());
            tvFecha.setText(DateFormat.getDateInstance().format(new Date(abono.getFecha())));
        } else {
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            tvFecha.setText(DateFormat.getDateInstance().format(new Date(System.currentTimeMillis())));
        }
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
            Button neutralButton = dialog.getButton(Dialog.BUTTON_NEUTRAL);

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double valorAbono = Double.valueOf(editTxtAbono.getText().toString().isEmpty() ?
                            "0" : editTxtAbono.getText().toString());
                    if (valorAbono == 0) {
                        editTxtAbono.setText("");
                        editTxtAbono.setError(getString(R.string.productos_error_required));
                    } else {
                        switch (mode){
                            case MODE_ADD:{
                                if (valorAbono > producto.getAdeudo()) {
                                    editTxtAbono.setError(getString(R.string.admonAbono_error_abonoBiger) +
                                            " ($" + producto.getAdeudo() + ")");
                                } else {
                                    abono = new Abono();
                                    abono.setValor(valorAbono);
                                    abono.setFecha(mCalendar.getTimeInMillis());

                                    presenter.addAbono(producto, abono, client);
                                }
                                break;
                            }
                            case MODE_EDIT:{
                                if (valorAbono > producto.getAdeudo()) {
                                    editTxtAbono.setError(getString(R.string.admonAbono_error_abonoBiger) +
                                            " ($" + producto.getAdeudo() + ")");
                                } else {
                                    abono.setValor(valorAbono);
                                    abono.setFecha(mCalendar.getTimeInMillis());

                                    presenter.updateAbono(producto, abono, client);
                                }
                                break;
                            }
                            case MODE_GRAL:{
                                double deudaTotal = getAdeudoTotal();
                                if (valorAbono > deudaTotal) {
                                    editTxtAbono.setError(getString(R.string.admonAbono_error_abonoBiger) +
                                            " ($" + deudaTotal + ")");
                                } else {
                                    presenter.addAbonoGral(productos, valorAbono, mCalendar.getTimeInMillis(), client);
                                }
                                break;
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

            neutralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity(), R.style.DialogFragmentTheme)
                            .setTitle(getString(R.string.admonAbono_title_confirmdelete))
                            .setMessage(getString(R.string.admonAbono_message_confirmdelete))
                            .setPositiveButton(getString(R.string.addclient_message_acept), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    presenter.deleteAbono(producto, abono, client);
                                }
                            })
                            .setNegativeButton(getString(R.string.addclient_message_cancel), null)
                            .show();
                }
            });

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTxtAbono, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void setupInjection() {
        app.getAdmonContactComponent(this, this).inject(this);
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
    public void abonoAdded(Abono abono) {
        Toast.makeText(getActivity(), R.string.admonAbono_message_abonoAdded,
                Toast.LENGTH_SHORT).show();
        ((AdmonAbonoAux)getActivity()).abonoAdded(abono);
        dismiss();
    }

    @Override
    public void abonoUpdated() {
        Toast.makeText(getActivity(), R.string.admonAbono_message_abonoAdded,
                Toast.LENGTH_SHORT).show();
        ((AdmonAbonoAux)getActivity()).abonoUpdated(abono);
        dismiss();
    }

    @Override
    public void abonoDeleted() {
        ((AdmonAbonoAux)getActivity()).abonoDeleted(abono);
        dismiss();
    }

    @Override
    public void abonoNotAdded() {
        producto.setAbono(producto.getAbono() - Double.valueOf(editTxtAbono.getText().toString()));
        editTxtAbono.setText("");
        editTxtAbono.setError(getString(R.string.addclient_error_add));
    }

    public double getAdeudoTotal() {
        double adeudo = 0;
        for (Producto producto : productos) {
            adeudo += producto.getAdeudo();
        }
        return adeudo;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tvFecha.setText(DateFormat.getDateInstance().format(new Date(mCalendar.getTimeInMillis())));
    }

    @OnClick(R.id.tvFecha)
    public void selectDate() {
        DialogoSelectorFecha dialogoSelectorFecha = new DialogoSelectorFecha();
        dialogoSelectorFecha.setOnDateSetListener(this);
        Bundle args = new Bundle();
        args.putLong(DialogoSelectorFecha.FECHA, mCalendar.getTimeInMillis());
        dialogoSelectorFecha.setArguments(args);
        dialogoSelectorFecha.show(getActivity().getSupportFragmentManager(), "selectoFecha");
    }


}
