package com.artec.mobile.clienti.signup.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.main.ui.MainActivity;
import com.artec.mobile.clienti.signup.SignUpPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    @Bind(R.id.editTxtEmail)
    EditText editTxtEmail;
    @Bind(R.id.editTxtPassword)
    EditText editTxtPassword;
    @Bind(R.id.btnRegister)
    Button btnRegister;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.layoutMainContainer)
    RelativeLayout layoutMainContainer;
    @Bind(R.id.editTxtUsername)
    EditText editTxtUsername;

    private ClientiApp app;

    @Inject
    SignUpPresenter signUpPresenter;
    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        app = (ClientiApp) getApplication();
        setupInjection();
        signUpPresenter.onCreate();
    }

    private void setupInjection() {
        app.getSignUpComponent(this).inject(this);
    }

    @Override
    protected void onDestroy() {
        signUpPresenter.onDrestoy();
        super.onDestroy();
    }

    @Override
    public void enableInputs() {
        setInputs(true);
    }

    @Override
    public void disableInputs() {
        setInputs(false);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnRegister)
    @Override
    public void handleSignUp() {
        if (validateFields()) {
            signUpPresenter.registerNewUser(
                    editTxtEmail.getText().toString(),
                    editTxtPassword.getText().toString(),
                    editTxtUsername.getText().toString());
        }
    }

    @Override
    public void navigateToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void loginError(String error) {
        editTxtPassword.setText("");
        String msgError = String.format(getString(R.string.login_user_message_signin), error);
        editTxtPassword.setError(msgError);
    }

    @Override
    public void newUserSuccess() {
        Snackbar.make(layoutMainContainer, R.string.signup_notice_message_useradded,
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void newUserError(String error) {
        editTxtPassword.setText("");
        String msgError = String.format(getString(R.string.signup_error_useradded), error);
        editTxtPassword.setError(msgError);
    }

    @Override
    public void setUsernameEmail(String email, String username) {
        if (email != null) {
            sharedPreferences.edit().putString(app.getEmailKey(), email).apply();
        }
        if (username != null){
            sharedPreferences.edit().putString(app.getUserName(), username).apply();
        }
    }

    private void setInputs(boolean enabled) {
        editTxtUsername.setEnabled(enabled);
        editTxtEmail.setEnabled(enabled);
        editTxtPassword.setEnabled(enabled);
        btnRegister.setEnabled(enabled);
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (editTxtEmail.getText().toString().isEmpty()){
            editTxtEmail.setError(getString(R.string.productos_error_required));
            isValid = false;
        }
        if (editTxtPassword.getText().toString().isEmpty()){
            editTxtPassword.setError(getString(R.string.productos_error_required));
            isValid = false;
        }
        if (editTxtUsername.getText().toString().isEmpty()){
            editTxtUsername.setError(getString(R.string.productos_error_required));
        }

        return isValid;
    }
}
