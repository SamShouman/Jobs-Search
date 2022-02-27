package com.example.jobproject2.Repositories;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.jobproject2.Interfaces.ISignUpActivityCallbacks;
import com.example.jobproject2.Interfaces.ISignUpActivityEvents;
import com.example.jobproject2.Models.User;
import com.example.jobproject2.Tools.Constants;
import com.example.jobproject2.Tools.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivityRepository {
    private ISignUpActivityCallbacks signUpActivityCallbacks;
    private ISignUpActivityEvents events;

    public void createAccount(Context context, FirebaseAuth auth, String firstName, String lastName, String email, String password, String mobileNb, String role) {

        ProgressDialog progressDialog = Utils.createAndShowProgressDialog(context);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                progressDialog.dismiss();
                FirebaseUser user = auth.getCurrentUser();
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

        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_REF_USERS).child(userId).setValue(user);
        events.saveUserDataLocally(userId, firstName + " " + lastName, role, mobileNb);
    }

    public void setSignUpActivityCallbacks(ISignUpActivityCallbacks signUpActivityCallbacks) {
        this.signUpActivityCallbacks = signUpActivityCallbacks;
    }

    public void setEvents(ISignUpActivityEvents events) {
        this.events = events;
    }
}
