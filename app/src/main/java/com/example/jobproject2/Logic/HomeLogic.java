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
import com.example.jobproject2.Repositories.HomeActivityRepository;
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
    private HomeActivityRepository repository = new HomeActivityRepository();

    public HomeLogic(IHomeFirebaseCallback callback) {
        repository.setCallback(callback);
    }

    public boolean checkValidation(String salary, String position, EditText phoneNbEdtTxt, EditText descriptionEdtTxt, DatabaseReference ref, Context ctx,
                                Uri filePath, StorageReference storageReference) {

        if (phoneNbEdtTxt.getText().toString().isEmpty()) {
            phoneNbEdtTxt.setError(ctx.getString(R.string.field_required));
            return false;
        } else {
            // edit data
            repository.saveNewData(salary, position, phoneNbEdtTxt.getText().toString(), descriptionEdtTxt.getText().toString(), ref, ctx);
            repository.uploadImage(filePath, ctx, storageReference, ref);
            return true;
        }
    }

    public void getUserDataFromDb(DatabaseReference ref, Context ctx) {
        repository.getUserDataFromDb(ref, ctx);
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
