package com.example.jobproject2.Interfaces;

import com.example.jobproject2.Models.User;

import java.util.ArrayList;

public interface IEmployeeHomeFirebaseCallback {
    void onGetAllUsersCallback(ArrayList<User> usersEmployeeArrayLst);
}
