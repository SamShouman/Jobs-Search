package com.example.jobproject2.Logic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.example.jobproject2.Interfaces.ISignUpActivityCallbacks;
import com.example.jobproject2.Models.User;
import com.example.jobproject2.R;
import com.example.jobproject2.Tools.Constants;
import com.example.jobproject2.Tools.SharedPreferencesManager;
import com.example.jobproject2.Tools.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivityLogic extends BasicLogic {

    private ISignUpActivityCallbacks signUpActivityCallbacks;

    public SignUpActivityLogic(Context context, Activity activity) {
        super(context, activity);
    }

    public void validateFields(TextInputLayout firstNameInputLyt, TextInputLayout lastNameInputLyt, TextInputLayout mobileNbInputLyt,
                               TextInputLayout emailInputLyt, TextInputLayout passwordInputLyt, TextInputEditText firstNameInputEditTxt,
                               TextInputEditText lastNameInputEditTxt, TextInputEditText mobileNbInputEditTxt,
                                TextInputEditText emailInputEditTxt, TextInputEditText passwordInputEditTxt, TextInputLayout companyNameInputLyt,
                               TextInputEditText companyNameEdtTxt, String role) {

        String firstName = firstNameInputEditTxt.getText().toString();
        String lastName = lastNameInputEditTxt.getText().toString();
        String email = emailInputEditTxt.getText().toString();
        String password = passwordInputEditTxt.getText().toString();
        String mobileNb = mobileNbInputEditTxt.getText().toString();
        String companyName = companyNameEdtTxt.getText().toString();

        boolean isEmpty = firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty();

        if(role.equals(Constants.ROLE_EMPLOYEE))
            isEmpty |= mobileNb.isEmpty();
        else
            isEmpty |= companyName.isEmpty();

        if (isEmpty) {
            showError(firstNameInputEditTxt, firstNameInputLyt);
            showError(lastNameInputEditTxt, lastNameInputLyt);
            showError(mobileNbInputEditTxt, mobileNbInputLyt);
            showError(emailInputEditTxt, emailInputLyt);
            showError(passwordInputEditTxt, passwordInputLyt);
            showError(companyNameEdtTxt, companyNameInputLyt);
        } else {
            boolean shouldCreateAccount = true;

            if (!email.matches(Constants.EMAIL_PATTERN)) {
                shouldCreateAccount = false;
                emailInputEditTxt.setError(activity.getString(R.string.email_not_valid));
            }

            if (password.length() < 8) {
                shouldCreateAccount = false;
                passwordInputLyt.setError(activity.getString(R.string.password_short));
            }

            if (shouldCreateAccount) {
                createAccount(firstName, lastName, email, password, mobileNb, role);
            }
        }
    }

    private void createAccount(String firstName, String lastName, String email, String password, String mobileNb, String role) {

        ProgressDialog progressDialog = Utils.createAndShowProgressDialog(context);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                progressDialog.dismiss();
                FirebaseUser user = mAuth.getCurrentUser();
                writeNewUser(user.getUid(), firstName, lastName, email, password, mobileNb, role);
                signUpActivityCallbacks.onSignUpSuccess();

            } else {
                progressDialog.dismiss();
                signUpActivityCallbacks.onSignUpFailure(task.getException().getMessage());
            }
        });
    }

    private void writeNewUser(String userId, String firstName, String lastName, String email, String password, String mobileNb, String role) {
        User user = new User(userId, firstName, lastName, email, password, mobileNb);
        user.setPassword(password);
        user.setRole(role);
        user.setPosition("N/A");
        user.setSalary("N/A");

        FirebaseDatabase.getInstance().getReference().child(Constants.NODE_USERS).child(userId).setValue(user);
        saveUserDataLocally(userId, firstName + " " + lastName, role, mobileNb);
    }

    private void saveUserDataLocally(String userId, String name, String role, String phoneNumber) {
        SharedPreferencesManager.setUserId(context, userId);
        SharedPreferencesManager.setName(context, name);
        SharedPreferencesManager.setRole(context, role);
        SharedPreferencesManager.setPhoneNb(context, phoneNumber);
    }

    private void showError(TextInputEditText textInputEditText, TextInputLayout textInputLayout) {
        if (textInputEditText.getText().toString().isEmpty())
            textInputLayout.setError(activity.getString(R.string.field_required));
    }

    public void setSignUpActivityCallbacks(ISignUpActivityCallbacks signUpActivityCallbacks) {
        this.signUpActivityCallbacks = signUpActivityCallbacks;
    }
}
