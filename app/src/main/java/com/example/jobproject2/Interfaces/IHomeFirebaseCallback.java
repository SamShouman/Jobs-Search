package com.example.jobproject2.Interfaces;

import com.example.jobproject2.Models.Position;
import com.example.jobproject2.Models.Salary;
import com.example.jobproject2.Models.User;

import java.util.ArrayList;

public interface IHomeFirebaseCallback {
    void onUserDataRetrieved(User userEmployee);
    void onPositionsRetrieved(ArrayList<Position> positionsArrayLst);
    void onSalariesRetrieved(ArrayList<Salary> salariesArrayLst);
    void onUpdateUserSuccess();
    void onImageUploaded();
}
