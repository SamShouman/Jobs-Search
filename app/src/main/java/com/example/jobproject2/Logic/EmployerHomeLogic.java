package com.example.jobproject2.Logic;

import android.content.Context;

import com.example.jobproject2.Interfaces.IEmployeeHomeFirebaseCallback;
import com.example.jobproject2.Models.User;
import com.example.jobproject2.Tools.Constants;
import com.example.jobproject2.Tools.SharedPreferencesManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import papaya.in.sendmail.SendMail;

public class EmployerHomeLogic {
    private IEmployeeHomeFirebaseCallback callback;

    public void getAllUsers(DatabaseReference ref) {
        ref.child(Constants.FIREBASE_REF_USERS).addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    collectAllUsers((Map<String,Object>) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) { callback.onGetAllUsersCallback(new ArrayList<>()); }
            });
    }

    private void collectAllUsers(Map<String, Object> users) {
        ArrayList<User> usersEmployeeArrayLst = new ArrayList<>();

        for (Map.Entry<String, Object> entry : users.entrySet()){
            Map user = (Map) entry.getValue();

            if(user.get("role").equals(Constants.ROLE_EMPLOYEE)) {
                User ue = new User();
                ue.setEmail((String) user.get("email"));
                ue.setFirstName((String) user.get("firstName"));
                ue.setLastName((String) user.get("lastName"));
                ue.setMobileNb((String) user.get("mobileNb"));
                ue.setRole((String) user.get("role"));
                ue.setUserId((String) user.get("userId"));
                ue.setDescription((String) user.get("description"));
                ue.setProfilePicture((String) user.get("profilePicture"));
                ue.setSalary((String) user.get("salary"));
                ue.setPosition((String) user.get("position"));

                usersEmployeeArrayLst.add(ue);
            }
        }

        callback.onGetAllUsersCallback(usersEmployeeArrayLst);
    }

    public ArrayList<User> filterList(ArrayList<User> usersArrayList, String newText) {
        ArrayList<User> filteredUsersArrayList = new ArrayList<>();

        newText = newText.toLowerCase();

        if(newText == null || newText.isEmpty())
            filteredUsersArrayList = usersArrayList;
        else
            for(int i = 0; i < usersArrayList.size(); i++)
                try { // wrap in try/catch in case something is null
                    if(usersArrayList.get(i).getMobileNb().toLowerCase().contains(newText)
                            || usersArrayList.get(i).getName().toLowerCase().contains(newText)
                            || usersArrayList.get(i).getRole().toLowerCase().contains(newText)
                            || usersArrayList.get(i).getSalary().toLowerCase().contains(newText)
                            || usersArrayList.get(i).getPosition().toLowerCase().contains(newText))

                        filteredUsersArrayList.add(usersArrayList.get(i));
                } catch (Exception e) {}

        return filteredUsersArrayList;
    }

    public void setCallback(IEmployeeHomeFirebaseCallback callback) {
        this.callback = callback;
    }

    public void sendEmail(Context context, User employee) {
        String companyName = SharedPreferencesManager.getCompanyName(context);

        SendMail mail = new SendMail(Constants.EMAIL_SENDER, Constants.EMAIL_PASSWORD,
                employee.getEmail(),
                "Jobs Search Profile View",
                "Hello " + employee.getName() + ", " + "a recruiter from " + companyName + " viewed your profile.");

        mail.execute();
    }
}
