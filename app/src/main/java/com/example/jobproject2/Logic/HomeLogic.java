package com.example.jobproject2.Logic;

import android.content.Context;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.jobproject2.Interfaces.IHomeFirebaseCallback;
import com.example.jobproject2.Models.BasicModel;
import com.example.jobproject2.Models.Position;
import com.example.jobproject2.Models.Salary;
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

import java.util.ArrayList;

public class HomeLogic {
    private HomeActivityRepository repository = new HomeActivityRepository();

    public HomeLogic(IHomeFirebaseCallback callback) {
        repository.setCallback(callback);
    }

    public boolean checkValidation(String salary, String position, String position2, String position3,
                                   EditText phoneNbEdtTxt, EditText descriptionEdtTxt, DatabaseReference ref, Context ctx,
                                    Uri filePath, StorageReference storageReference) {

        if (phoneNbEdtTxt.getText().toString().isEmpty()) {
            phoneNbEdtTxt.setError(ctx.getString(R.string.field_required));
            return false;
        } else {
            // edit data
            repository.saveNewData(salary, position, position2, position3, phoneNbEdtTxt.getText().toString(), descriptionEdtTxt.getText().toString(), ref, ctx);
            repository.uploadImage(filePath, ctx, storageReference, ref);
            return true;
        }
    }

    public void getUserDataFromDb(DatabaseReference ref, Context ctx) {
        repository.getUserDataFromDb(ref, ctx);
    }

    public int getUserPositionIndex(String position, ArrayList<Position> arrayLst) {
        int index = 0;
        for(int i=0; i< arrayLst.size(); i++)
            if(arrayLst.get(i).getId().equals(position)) {
                index = i;
                break;
            }

        return index;
    }

    public int getUserSalaryIndex(String salary, ArrayList<Salary> arrayLst) {
        int index = 0;
        for(int i=0; i< arrayLst.size(); i++)
            if(arrayLst.get(i).getId().equals(salary)) {
                index = i;
                break;
            }

        return index;
    }

    public <T> ArrayList<String> getNames(ArrayList<T> arrayLst) {
        ArrayList<String> res = new ArrayList<>();

        for(T p: arrayLst) {
            BasicModel bm = (BasicModel) p;
            res.add(bm.getName());
        }

        return  res;
    }

    public void getPositionsFromDb(DatabaseReference ref) {
        repository.getPositions(ref);
    }

    public void getSalariesFromDb(DatabaseReference ref) {
        repository.getSalaries(ref);
    }
}
