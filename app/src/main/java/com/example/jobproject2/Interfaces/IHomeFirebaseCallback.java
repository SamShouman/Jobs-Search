package com.example.jobproject2.Interfaces;

import com.example.jobproject2.Models.User;

public interface IHomeFirebaseCallback {
    void onUserDataRetrieved(User userEmployee);
    void onUpdateUserSuccess();
    void onImageUploaded();
}
