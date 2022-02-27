package com.example.jobproject2.Repositories;

import com.example.jobproject2.Interfaces.ILoginActivityCallbacks;
import com.example.jobproject2.Interfaces.ILoginActivityEvents;
import com.example.jobproject2.Tools.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LoginActivityRepository {
    private ILoginActivityCallbacks loginActivityCallbacks;
    private ILoginActivityEvents events;

    public void authenticate(FirebaseAuth auth, DatabaseReference ref, String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                getAllUsers(ref, email, password);
            } else
                loginActivityCallbacks.onLoginFailure(task.getException().getMessage());
        });
    }


    public void getAllUsers(DatabaseReference ref, String email, String password) {
        ref.child(Constants.FIREBASE_REF_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               events.getUserIdByEmail(email, password, (Map<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void setLoginActivityCallbacks(ILoginActivityCallbacks loginActivityCallbacks) {
        this.loginActivityCallbacks = loginActivityCallbacks;
    }

    public void setEvents(ILoginActivityEvents events) {
        this.events = events;
    }
}
