package com.example.jobproject2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobproject2.Interfaces.ILoginActivityCallbacks;
import com.example.jobproject2.Logic.LoginActivityLogic;
import com.example.jobproject2.Tools.Constants;
import com.example.jobproject2.Tools.SharedPreferencesManager;
import com.example.jobproject2.Tools.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements ILoginActivityCallbacks {
    private TextInputLayout emailInputLyt, passwordInputLyt;
    private TextInputEditText emailInputEditTxt, passwordInputEditTxt;
    private TextView createNewAccountTxtV;
    private MaterialButton loginBtn;

    private ProgressDialog progressDialog;
    private LoginActivityLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        emailInputLyt = findViewById(R.id.emailInputLyt);
        emailInputEditTxt = findViewById(R.id.emailInputEditTxt);

        passwordInputLyt = findViewById(R.id.passwordInputLyt);
        passwordInputEditTxt = findViewById(R.id.passwordInputEditTxt);

        createNewAccountTxtV = findViewById(R.id.createNewAccountTxtV);
        loginBtn = findViewById(R.id.loginBtn);

        logic = new LoginActivityLogic(this, this);
        logic.setLoginActivityCallbacks(this);

        loginIfSharedPreferencesExist();
        setListeners();
    }

    private void loginIfSharedPreferencesExist() {
        if (!SharedPreferencesManager.getUserId(getApplicationContext()).isEmpty()) {
            sendUserToNextActivity(SharedPreferencesManager.getRole(getApplicationContext()));
        }
    }

    private void setListeners() {
        createNewAccountTxtV.setOnClickListener(view -> {
            Intent i = new Intent(this, SignUpActivity.class);
            startActivity(i);
        });

        loginBtn.setOnClickListener((View.OnClickListener) v -> performAuthentication());
    }

    private void performAuthentication() {
        String email = emailInputEditTxt.getText().toString();
        String password = passwordInputEditTxt.getText().toString();

        // TODO CHECK IF USER ENTERED HIS EMAIL AND PASSWORD

        showProgressDialog();
        logic.authenticate(email, password);
    }

    private void showProgressDialog() {
        progressDialog = Utils.createAndShowProgressDialog(this);
    }

    private void sendUserToNextActivity(String role) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        if (!role.equals(Constants.ROLE_EMPLOYEE)) {
            intent = new Intent(LoginActivity.this, EmployerHomeActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginSuccess(String role) {
        progressDialog.dismiss();
        sendUserToNextActivity(role);
    }

    @Override
    public void onLoginFailure(String exceptionMsg) {
        progressDialog.dismiss();
        Toast.makeText(this, exceptionMsg, Toast.LENGTH_SHORT).show();
    }
}