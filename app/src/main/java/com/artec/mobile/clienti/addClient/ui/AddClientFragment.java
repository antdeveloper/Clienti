package com.artec.mobile.clienti.addClient.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.addClient.AddClientPresenter;
import com.artec.mobile.clienti.libs.Constants;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ANICOLAS on 03/07/2016.
 */
public class AddClientFragment extends DialogFragment implements AddClientView,
        DialogInterface.OnShowListener {

    @Bind(R.id.editTxtEmail)
    EditText editTxtEmail;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.rbNativeUser)
    RadioButton rbNativeUser;
    @Bind(R.id.rbCustomUser)
    RadioButton rbCustomUser;
    @Bind(R.id.wrapperEmail)
    TextInputLayout wrapperEmail;

    @Inject
    AddClientPresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;

    private String myUsername;

    private ClientiApp app;

    public AddClientFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogFragmentTheme)
                .setTitle(R.string.addclient_message_title)
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add_client, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);

        app = (ClientiApp) getActivity().getApplication();
        setupInjection();
        setupRadioButtons();
        presenter.onShow();

        myUsername = sharedPreferences.getString(app.getUserName(), getString(R.string.app_name));
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
                    if (editTxtEmail.getText().toString().isEmpty()){
                        editTxtEmail.setError(getString(R.string.productos_error_required));
                    }else {
                        String email = editTxtEmail.getText().toString();
                        email = email.replace(" ", ".");
                        email = email.toLowerCase();
                        presenter.addContact(rbNativeUser.isChecked() ?
                                email : email + Constants.DOMINIO_CLIENTI, myUsername,
                                rbNativeUser.isChecked() ? "" : editTxtEmail.getText().toString());
                    }
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
        app.getAddClientComponent(this, this).inject(this);
    }

    private void setupRadioButtons() {

    }

    @Override
    public void showInput() {
        editTxtEmail.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInput() {
        editTxtEmail.setVisibility(View.GONE);
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
    public void contactAdded() {
        Toast.makeText(getActivity(), R.string.addclient_message_clientadded, Toast.LENGTH_SHORT)
                .show();
        dismiss();
    }

    @Override
    public void contactNotAdded() {
        editTxtEmail.setText("");
        editTxtEmail.setError(getString(R.string.addclient_error_add));
    }

    @OnClick({R.id.rbNativeUser, R.id.rbCustomUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbNativeUser:
                editTxtEmail.setHint(R.string.login_message_email);
                wrapperEmail.setHint(getString(R.string.login_message_email));
                break;
            case R.id.rbCustomUser:
                editTxtEmail.setHint(R.string.productos_hint_name);
                wrapperEmail.setHint(getString(R.string.productos_hint_name));
                break;
        }
    }
}
