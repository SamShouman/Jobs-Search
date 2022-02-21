package com.example.jobproject2.Logic;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BasicLogic {
    Activity activity;
    Context context;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;

    public BasicLogic() {
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();
    }

    public BasicLogic(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();
    }
}
