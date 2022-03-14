package com.example.jobproject2.Repositories;

import androidx.annotation.NonNull;

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

        ref.child(Constants.FIREBASE_REF_FAV_USERS).child(currentUserId).push().setValue(employee.getUserId())
                .addOnCompleteListener(task -> {

            callback.onAddFavouriteUserSuccess(task.isSuccessful());
        });
    }

    public void getAllUsers(DatabaseReference ref, boolean shouldGetFavourites, String userId) {
        DatabaseReference newRef = ref.child(Constants.FIREBASE_REF_USERS);

        newRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> allUsersMap = (Map<String, Object>) dataSnapshot.getValue();
                        if (shouldGetFavourites) {
                            ref.child(Constants.FIREBASE_REF_FAV_USERS).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    events.collectFavouriteUsers(allUsersMap, (Map<String, String>) snapshot.getValue(), callback);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            events.collectAllUsers(allUsersMap, callback);
                        }
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
