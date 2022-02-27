package com.example.jobproject2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jobproject2.Interfaces.ISignUpActivityCallbacks;
import com.example.jobproject2.Logic.SignUpActivityLogic;
import com.example.jobproject2.Tools.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity implements ISignUpActivityCallbacks {
    private Toolbar toolbar;
    private TextInputLayout emailInputLyt, passwordInputLyt, firstNameInputLyt, lastNameInputLyt, mobileNbInputLyt, companyNameInputLyt;
    private TextInputEditText emailInputEditTxt, passwordInputEditTxt, firstNameInputEditTxt, lastNameInputEditTxt, mobileNbInputEditTxt, companyNameInputEditTxt;
    private Switch userTypeSwitch;
    private MaterialButton signUpBtn;
    private TextView switchEmployeeTxtV, switchEmployerTxtV;

    private ProgressDialog progressDialog;
    private SignUpActivityLogic logic;

    private String currentRole = Constants.ROLE_EMPLOYEE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        setListeners();
    }

    public void init() {
        logic = new SignUpActivityLogic(this, this, this);

        toolbar = findViewById(R.id.toolbar);

        userTypeSwitch = findViewById(R.id.userTypeSwitch);

        emailInputLyt = findViewById(R.id.emailInputLyt);
        emailInputEditTxt = findViewById(R.id.emailInputEditTxt);

        passwordInputLyt = findViewById(R.id.passwordInputLyt);
        passwordInputEditTxt = findViewById(R.id.passwordInputEditTxt);

        firstNameInputLyt = findViewById(R.id.firstNameInputLyt);
        firstNameInputEditTxt = findViewById(R.id.firstNameInputEditTxt);

        lastNameInputLyt = findViewById(R.id.lastNameInputLyt);
        lastNameInputEditTxt = findViewById(R.id.lastNameInputEditTxt);

        mobileNbInputLyt = findViewById(R.id.mobileNbInputLyt);
        mobileNbInputEditTxt = findViewById(R.id.mobileNbInputEditTxt);

        companyNameInputLyt = findViewById(R.id.companyNameInputLyt);
        companyNameInputEditTxt = findViewById(R.id.companyNameInputEditTxt);

        switchEmployeeTxtV = findViewById(R.id.switchEmployeeTxtV);
        switchEmployerTxtV = findViewById(R.id.switchEmployerTxtV);

        signUpBtn = findViewById(R.id.signUpBtn);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListeners() {
        userTypeSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) { // employer checked
                currentRole = Constants.ROLE_EMPLOYER;
                companyNameInputLyt.setVisibility(View.VISIBLE);
                mobileNbInputLyt.setVisibility(View.GONE);
            } else {
                currentRole = Constants.ROLE_EMPLOYEE;
                companyNameInputLyt.setVisibility(View.GONE);
                mobileNbInputLyt.setVisibility(View.VISIBLE);
            }
        });

        switchEmployerTxtV.setOnClickListener(view -> userTypeSwitch.setChecked(true));
        switchEmployeeTxtV.setOnClickListener(view -> userTypeSwitch.setChecked(false));

        signUpBtn.setOnClickListener(v -> {
            logic.validateFields(firstNameInputLyt, lastNameInputLyt, mobileNbInputLyt,
                    emailInputLyt,passwordInputLyt, firstNameInputEditTxt,
                    lastNameInputEditTxt, mobileNbInputEditTxt, emailInputEditTxt, passwordInputEditTxt, companyNameInputLyt, companyNameInputEditTxt, currentRole);
        });
    }

    private void sendUserToNextActivity() {
        Intent intent;
        if(currentRole.equals(Constants.ROLE_EMPLOYEE))
            intent = new Intent(SignUpActivity.this, HomeActivity.class);
        else
            intent = new Intent(SignUpActivity.this, EmployerHomeActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onSignUpSuccess() {
        sendUserToNextActivity();
    }

    @Override
    public void onSignUpFailure(String exceptionMsg) {
        Toast.makeText(this, exceptionMsg, Toast.LENGTH_SHORT).show();
    }
}