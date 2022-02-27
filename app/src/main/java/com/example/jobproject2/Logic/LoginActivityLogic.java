package com.example.jobproject2.Logic;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.jobproject2.Interfaces.ILoginActivityCallbacks;
import com.example.jobproject2.Interfaces.ILoginActivityEvents;
import com.example.jobproject2.R;
import com.example.jobproject2.Repositories.LoginActivityRepository;
import com.example.jobproject2.Tools.Constants;
import com.example.jobproject2.Tools.SharedPreferencesManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LoginActivityLogic extends BasicLogic implements ILoginActivityEvents {
    private ILoginActivityCallbacks loginActivityCallbacks;
    private LoginActivityRepository repository = new LoginActivityRepository();

    public LoginActivityLogic(Context context, Activity activity, ILoginActivityCallbacks callbacks) {
        super(context, activity);
        loginActivityCallbacks = callbacks;
        repository.setLoginActivityCallbacks(callbacks);
        repository.setEvents(this);
    }

    public void authenticate(String email, String password) {
        repository.authenticate(mAuth, mRef, email, password);
    }

    @Override
    public void getUserIdByEmail(String email, String password, Map<String, Object> users) {
        String userId = "", role = "", name = "", phoneNb = "", companyName = "";
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            Map user = (Map) entry.getValue();

            if (user.get("email").equals(email)) {
                role = (String) user.get("role");
                userId = (String) user.get("userId");
                name = (String) user.get("name");
                phoneNb = (String) user.get("mobileNb");
                companyName = (String) user.get("companyName");
                break;
            }
        }

        if (userId.isEmpty()) {
            loginActivityCallbacks.onLoginFailure(context.getString(R.string.user_not_found));
        } else {
            SharedPreferencesManager.setUserId(context, userId);
            SharedPreferencesManager.setName(context, name);
            SharedPreferencesManager.setPhoneNb(context, phoneNb);
            SharedPreferencesManager.setRole(context, role);
            SharedPreferencesManager.setCompanyName(context, companyName);

            loginActivityCallbacks.onLoginSuccess(role);
        }
    }
}
