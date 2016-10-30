package com.artec.mobile.clienti.login.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.artec.mobile.clienti.main.ui.MainActivity;
import com.artec.mobile.clienti.ClientiApp;
import com.artec.mobile.clienti.R;
import com.artec.mobile.clienti.login.LoginPresenter;
import com.artec.mobile.clienti.signup.ui.SignUpActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView{

    @Bind(R.id.editTxtEmail)
    EditText editTxtEmail;
    @Bind(R.id.editTxtPassword)
    EditText editTxtPassword;
    @Bind(R.id.btnSignin)
    Button btnSignin;
    @Bind(R.id.btnRegister)
    Button btnRegister;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.layoutMainContainer)
    RelativeLayout layoutMainContainer;

    private ClientiApp app;

    @Inject
    LoginPresenter loginPresenter;
    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.login_title);
        }
        app = (ClientiApp)getApplication();
        setupInjection();
        loginPresenter.onCreate();
        String email = sharedPreferences.getString(app.getEmailKey(), null);
        loginPresenter.validateLogin(email, null);
    }

    private void setupInjection() {
        app.getLoginComponent(this, this).inject(this);
    }

    @Override
    protected void onDestroy() {
        loginPresenter.onDrestoy();
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
    public void handleSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @OnClick(R.id.btnSignin)
    @Override
    public void handleSignIn() {
        if (validateFields()) {
            loginPresenter.validateLogin(
                    editTxtEmail.getText().toString(), editTxtPassword.getText().toString());
        }
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

        return isValid;
    }

    @Override
    public void navigateToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void loginError(String error) {
        editTxtPassword.setText("");
        String msgError = String.format(getString(R.string.login_user_message_signin), error);
        editTxtPassword.setError(msgError);
    }

    @Override
    public void setUsernameEmail(String email, String username) {
        if (email != null){
            sharedPreferences.edit().putString(app.getEmailKey(), email).commit();
            sharedPreferences.edit().putString(app.getUserName(), username).commit();
        }
    }

    private void setInputs(boolean enabled){
        editTxtEmail.setEnabled(enabled);
        editTxtPassword.setEnabled(enabled);
        btnSignin.setEnabled(enabled);
        btnRegister.setEnabled(enabled);
    }
}
