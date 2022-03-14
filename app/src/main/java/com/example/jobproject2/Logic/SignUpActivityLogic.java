package com.example.jobproject2.Logic;

import android.app.Activity;
import android.content.Context;

import com.example.jobproject2.Interfaces.ISignUpActivityCallbacks;
import com.example.jobproject2.Interfaces.ISignUpActivityEvents;
import com.example.jobproject2.R;
import com.example.jobproject2.Repositories.SignUpActivityRepository;
import com.example.jobproject2.Tools.Constants;
import com.example.jobproject2.Tools.SharedPreferencesManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivityLogic extends BasicLogic implements ISignUpActivityEvents {
    private SignUpActivityRepository repository = new SignUpActivityRepository();

    public SignUpActivityLogic(Context context, Activity activity, ISignUpActivityCallbacks callbacks) {
        super(context, activity);
        repository.setSignUpActivityCallbacks(callbacks);
        repository.setEvents(this);
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
        String mobileNb = mobileNbInputEditTxt.getText().toString(); // IN A REAL WORLD APPLICATION, AN OTP MUST BE SENT TO VERIFY THE PHONE NB
        String companyName = companyNameEdtTxt.getText().toString();

        boolean isEmpty = firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty();

        if (role.equals(Constants.ROLE_EMPLOYEE))
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

            if (!firstName.matches(Constants.NAME_PATTERN)) {
                shouldCreateAccount = false;
                firstNameInputLyt.setError(activity.getString(R.string.name_not_valid));
            }

            if (!lastName.matches(Constants.NAME_PATTERN)) {
                shouldCreateAccount = false;
                lastNameInputLyt.setError(activity.getString(R.string.name_not_valid));
            }

            if (password.length() < 8) {
                shouldCreateAccount = false;
                passwordInputLyt.setError(activity.getString(R.string.password_short));
            }

            String userCountryCode = mobileNbDoesNotContainCountryCode(mobileNb);
            String mobileNbWithoutCountryCodeAnd0 = "";

            if (userCountryCode.isEmpty()) { // user did not enter a country code
                shouldCreateAccount = false;
                mobileNbInputLyt.setError(activity.getString(R.string.country_code_required));
            }

            if (shouldCreateAccount) {
                String mobileNbWithoutCountryCode = mobileNb.replace("+" + userCountryCode, "");

                if(mobileNbWithoutCountryCode.toCharArray()[0] == '0') { // nb starts with 0, remove it
                     mobileNbWithoutCountryCodeAnd0 = mobileNbWithoutCountryCode.substring(1);
                } else {
                    mobileNbWithoutCountryCodeAnd0 = mobileNbWithoutCountryCode;
                }

                createAccount(firstName, lastName, email, password, "+" + userCountryCode + mobileNbWithoutCountryCodeAnd0, role);
            }
        }
    }

    private String mobileNbDoesNotContainCountryCode(String mobileNb) {
        String userCountryDialCode = "";

        String[] countryCodeArray = context.getResources().getStringArray(R.array.DialingCountryCode);

        for (int i = 0; i < countryCodeArray.length; i++) {
            String countryDialNb = countryCodeArray[i].split(",")[0];

            if (mobileNb.startsWith("+" + countryDialNb)) {
                userCountryDialCode = countryDialNb;
                break;
            }
        }

        return userCountryDialCode;
    }

    private void createAccount(String firstName, String lastName, String email, String password, String mobileNb, String role) {
        repository.createAccount(context, mAuth, firstName, lastName, email, password, mobileNb, role);
    }

    @Override
    public void saveUserDataLocally(String userId, String name, String role, String phoneNumber) {
        SharedPreferencesManager.setUserId(context, userId);
        SharedPreferencesManager.setName(context, name);
        SharedPreferencesManager.setRole(context, role);
        SharedPreferencesManager.setPhoneNb(context, phoneNumber);
    }

    private void showError(TextInputEditText textInputEditText, TextInputLayout textInputLayout) {
        if (textInputEditText.getText().toString().isEmpty())
            textInputLayout.setError(activity.getString(R.string.field_required));
    }
}
