package com.example.jobproject2.Repositories;

import com.example.jobproject2.Interfaces.IEmployeeHomeFirebaseCallback;
import com.example.jobproject2.Interfaces.IEmployerHomeEvents;
import com.example.jobproject2.Models.FavouriteUser;
import com.example.jobproject2.Models.User;
import com.example.jobproject2.Tools.Constants;
import com.example.jobproject2.Tools.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class EmployerHomeRepository {
    private IEmployeeHomeFirebaseCallback callback;
    private IEmployerHomeEvents events;

    public void addFavouriteUser(DatabaseReference ref, User employee, String currentUserId) {
        FavouriteUser favouriteUser = new FavouriteUser();
        favouriteUser.setId(employee.getUserId());

        ref.child(Constants.FIREBASE_REF_FAV_USERS).child(currentUserId).child(employee.getUserId())
                .setValue(favouriteUser).addOnCompleteListener(task -> {

            callback.onAddFavouriteUserSuccess(task.isSuccessful());
        });
    }

    public void getAllUsers(DatabaseReference ref, boolean shouldGetFavourites, String userId) {
        DatabaseReference newRef = ref.child(Constants.FIREBASE_REF_USERS);

        if (shouldGetFavourites)
            newRef = ref.child(Constants.FIREBASE_REF_FAV_USERS).child(userId);

        newRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        events.collectAllUsers((Map<String, Object>) dataSnapshot.getValue(), callback);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onGetAllUsersCallback(new ArrayList<>());
                    }
                });
    }

    public void setCallback(IEmployeeHomeFirebaseCallback callback) {
        this.callback = callback;
    }

    public void setEvents(IEmployerHomeEvents events) {
        this.events = events;
    }
}
