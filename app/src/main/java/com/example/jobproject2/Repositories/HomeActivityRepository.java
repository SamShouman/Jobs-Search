package com.example.jobproject2.Repositories;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.jobproject2.Interfaces.IHomeFirebaseCallback;
import com.example.jobproject2.Models.Position;
import com.example.jobproject2.Models.Salary;
import com.example.jobproject2.Models.User;
import com.example.jobproject2.Tools.Constants;
import com.example.jobproject2.Tools.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeActivityRepository {
    private IHomeFirebaseCallback callback;

    public void saveNewData(String salary, String position, String position2, String position3, String phoneNb, String description, DatabaseReference ref, Context ctx) {
        ref = ref.child(Constants.FIREBASE_REF_USERS).child(SharedPreferencesManager.getUserId(ctx));

        ref.child("mobileNb").setValue(phoneNb.trim());
        ref.child("description").setValue(description.trim());
        ref.child("salary").setValue(salary);
        ref.child("position").setValue(position);
        ref.child("position2").setValue(position2);
        ref.child("position3").setValue(position3);

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

    public void getPositions(DatabaseReference ref) {
        ref.child(Constants.FIREBASE_REF_POSITIONS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Position> positionsArrayLst = new ArrayList<>();

                for (DataSnapshot s : snapshot.getChildren()) {
                    positionsArrayLst.add(s.getValue(Position.class));
                }

                callback.onPositionsRetrieved(positionsArrayLst);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void getSalaries(DatabaseReference ref) {
        ref.child(Constants.FIREBASE_REF_SALARIES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Salary> salariesArrayLst = new ArrayList<>();

                for (DataSnapshot s : snapshot.getChildren()) {
                    salariesArrayLst.add(s.getValue(Salary.class));
                }

                callback.onSalariesRetrieved(salariesArrayLst);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void setCallback(IHomeFirebaseCallback callback) {
        this.callback = callback;
    }
}
