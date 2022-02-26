package com.example.jobproject2.Logic;

import android.content.Context;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.jobproject2.Interfaces.IHomeFirebaseCallback;
import com.example.jobproject2.Models.User;
import com.example.jobproject2.R;
import com.example.jobproject2.Tools.Constants;
import com.example.jobproject2.Tools.SharedPreferencesManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class HomeLogic {
    private IHomeFirebaseCallback callback;

    public void checkValidation(String salary, String position, EditText phoneNbEdtTxt, EditText descriptionEdtTxt, DatabaseReference ref, Context ctx,
                                Uri filePath, StorageReference storageReference) {

        if (phoneNbEdtTxt.getText().toString().isEmpty()) {
            phoneNbEdtTxt.setError("Enter your phone nb");
        } else if (phoneNbEdtTxt.getText().toString().length() < 8) {
            phoneNbEdtTxt.setError("Phone number must be at least 8 characters");
        } else if (descriptionEdtTxt.getText().toString().length() < 0) {
            phoneNbEdtTxt.setError("Enter your role");
        } else {
            // edit data
            saveNewData(salary, position, phoneNbEdtTxt.getText().toString(), descriptionEdtTxt.getText().toString(), ref, ctx);
            uploadImage(filePath, ctx, storageReference, ref);
        }
    }

    private void saveNewData(String salary, String position, String phoneNb, String description, DatabaseReference ref, Context ctx) {
        ref = ref.child(Constants.FIREBASE_REF_USERS).child(SharedPreferencesManager.getUserId(ctx));

        ref.child("mobileNb").setValue(phoneNb);
        ref.child("description").setValue(description);
        ref.child("salary").setValue(salary);
        ref.child("position").setValue(position);

        callback.onUpdateUserSuccess();
    }

    private void uploadImage(Uri filePath, Context ctx, StorageReference storageReference, DatabaseReference dbRef) {
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setCallback(IHomeFirebaseCallback callback) {
        this.callback = callback;
    }

    public int getUserPositionIndex(Context context, String position) {
        String[] positionsArray = context.getResources().getStringArray(R.array.positionsArray);
        int index = 0;
        for(int i=0; i< positionsArray.length; i++)
            if(positionsArray[i].equals(position)) {
                index = i;
                break;
            }

        return index;
    }

    public int getUserSalaryIndex(Context context, String salary) {
        String[] salariesArray = context.getResources().getStringArray(R.array.salariesArray);
        int index = 0;
        for(int i=0; i< salariesArray.length; i++)
            if(salariesArray[i].equals(salary)) {
                index = i;
                break;
            }

        return index;
    }
}
