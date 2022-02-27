package com.example.jobproject2.Repositories;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.jobproject2.Interfaces.IHomeFirebaseCallback;
import com.example.jobproject2.Models.User;
import com.example.jobproject2.Tools.Constants;
import com.example.jobproject2.Tools.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class HomeActivityRepository {
    private IHomeFirebaseCallback callback;

    public void saveNewData(String salary, String position, String phoneNb, String description, DatabaseReference ref, Context ctx) {
        ref = ref.child(Constants.FIREBASE_REF_USERS).child(SharedPreferencesManager.getUserId(ctx));

        ref.child("mobileNb").setValue(phoneNb);
        ref.child("description").setValue(description);
        ref.child("salary").setValue(salary);
        ref.child("position").setValue(position);

        callback.onUpdateUserSuccess();
    }

    public void uploadImage(Uri filePath, Context ctx, StorageReference storageReference, DatabaseReference dbRef) {
        if (filePath != null) {

            StorageReference ref = storageReference.child("images/"+ SharedPreferencesManager.getUserId(ctx));

            ref.putFile(filePath).addOnSuccessListener(
                    taskSnapshot -> {
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                            dbRef.child(Constants.FIREBASE_REF_USERS).child(SharedPreferencesManager.getUserId(ctx))
                                    .child("profilePicture").setValue(task.getResult().toString());
                            callback.onImageUploaded();
                        });
                    }).addOnFailureListener(e -> Toast.makeText(ctx,"Failed " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    public void getUserDataFromDb(DatabaseReference ref, Context ctx) {
        ref.child(Constants.FIREBASE_REF_USERS).child(SharedPreferencesManager.getUserId(ctx)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user= snapshot.getValue(User.class);
                callback.onUserDataRetrieved(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void setCallback(IHomeFirebaseCallback callback) {
        this.callback = callback;
    }
}
