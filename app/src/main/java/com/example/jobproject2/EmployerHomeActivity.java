package com.example.jobproject2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobproject2.Adapters.UsersAdapter;
import com.example.jobproject2.Interfaces.IEmployeeHomeFirebaseCallback;
import com.example.jobproject2.Interfaces.IUsersAdapterEvents;
import com.example.jobproject2.Logic.EmployerHomeLogic;
import com.example.jobproject2.Models.User;
import com.example.jobproject2.Tools.SharedPreferencesManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EmployerHomeActivity extends AppCompatActivity implements IUsersAdapterEvents, IEmployeeHomeFirebaseCallback {
    private RecyclerView employeesRecV;
    private SearchView searchView;
    private ProgressDialog progressDialog;
    private ImageButton signOutImgBtn;

    private DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference();
    private RecyclerView.LayoutManager layoutManager;
    private UsersAdapter adapter;
    private EmployerHomeLogic mLogic = new EmployerHomeLogic();
    private ArrayList<User> defaultUsersArrayLst = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_home);

        init();
    }

    public void init() {
        employeesRecV = findViewById(R.id.employeesRecV);
        searchView = findViewById(R.id.searchView);
        signOutImgBtn = findViewById(R.id.signOutImgBtn);

        layoutManager = new LinearLayoutManager(this);

        showProgressDialog();

        mLogic.setCallback(this);
        mLogic.getAllUsers(mRef);

        setListeners();
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting data...");
        progressDialog.setTitle("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void setListeners() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<User> filteredUsersArrayList = mLogic.filterList(defaultUsersArrayLst, newText);
                setUsersRecyclerViewAdapter(filteredUsersArrayList);
                return false;
            }
        });

        signOutImgBtn.setOnClickListener(view -> {
            SharedPreferencesManager.clearSharedPreferences(getApplicationContext());
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void setUsersRecyclerViewAdapter(ArrayList<User> newArrayList) {
        adapter = new UsersAdapter(newArrayList, getApplicationContext());
        adapter.setEvents(this);
        employeesRecV.setLayoutManager(layoutManager);
        employeesRecV.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(User employee) {
        // go to user profile
        Intent i =new Intent(this, HomeActivity.class);

        i.putExtra("name", employee.getName());
        i.putExtra("phone", employee.getMobileNb());
        i.putExtra("profilePicture", employee.getProfilePicture());
        i.putExtra("description", employee.getDescription());
        i.putExtra("salary", employee.getSalary());
        i.putExtra("position", employee.getPosition());

        startActivity(i);

        mLogic.sendEmail(getApplicationContext(), employee);
    }

    @Override
    public void onEmailImageButtonClicked(User employee) {
        sendEmail(employee.getEmail());
    }

    @Override
    public void onPhoneImageButtonClicked(User employee) {
        makeTheCall(employee.getMobileNb());
    }

    @Override
    public void onGetAllUsersCallback(ArrayList<User> usersEmployeeArrayLst) {
        defaultUsersArrayLst = usersEmployeeArrayLst;

        // setup the recycler view
        setUsersRecyclerViewAdapter(usersEmployeeArrayLst);

        progressDialog.dismiss();
    }

    public void makeTheCall(String phoneNb) {
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setData(Uri.parse("tel:" + phoneNb));
        startActivity(call);
    }

    public void sendEmail(String emailTo) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{emailTo});
        i.putExtra(Intent.EXTRA_SUBJECT, "Job Offer");
        i.putExtra(Intent.EXTRA_TEXT   , "This is a job offer from my application.");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}