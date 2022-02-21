package com.example.jobproject2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobproject2.Interfaces.IHomeFirebaseCallback;
import com.example.jobproject2.Logic.HomeLogic;
import com.example.jobproject2.Models.User;
import com.example.jobproject2.Tools.SharedPreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements IHomeFirebaseCallback {

    private TextView titleTxtV;
    private TextInputLayout nameInputLyt, mobileNbInputLyt, descriptionInputLyt;
    private TextInputEditText nameInputEdtTxt, mobileNbInputEdtTxt, descriptionInputEdtTxt;
    private CircleImageView profilePicImgV;
    private MaterialButton saveBtn;
    private ImageButton signOutImgBtn, editImgBtn;
    private Spinner salariesSpinner, positionsSpinner;
    private ProgressDialog progressDialog;
    private HomeLogic mLogic = new HomeLogic();
    private DatabaseReference mRef;

    private ArrayAdapter<String> salariesAdapter;
    private ArrayAdapter positionsAdapter;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 1; // used to check if we returned from gallery after choosing an image

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
    }

    private void init() {
        titleTxtV = findViewById(R.id.titleTxtV);

        salariesSpinner = findViewById(R.id.salariesSpinner);
        positionsSpinner = findViewById(R.id.positionsSpinner);

        nameInputLyt = findViewById(R.id.nameInputLyt);
        nameInputEdtTxt = findViewById(R.id.nameInputEditTxt);

        mobileNbInputLyt = findViewById(R.id.mobileNbInputLyt);
        descriptionInputLyt = findViewById(R.id.descriptionInputLyt);

        mobileNbInputEdtTxt = findViewById(R.id.mobileNbInputEditTxt);
        descriptionInputEdtTxt = findViewById(R.id.descriptionInputEditTxt);

        saveBtn = findViewById(R.id.saveBtn);
        signOutImgBtn = findViewById(R.id.signOutImgBtn);
        profilePicImgV = findViewById(R.id.profilePicImgV);
        editImgBtn = findViewById(R.id.editImgBtn);

        loadSpinnersData();
        handleLoadingUserData();
    }

    private void loadSpinnersData() {
        String[] positionsArray = getResources().getStringArray(R.array.positionsArray);
        String[] salariesArray = getResources().getStringArray(R.array.salariesArray);

        positionsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, positionsArray);
        positionsSpinner.setAdapter(positionsAdapter);

        salariesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, salariesArray);
        salariesSpinner.setAdapter(salariesAdapter);
    }

    private void handleLoadingUserData() {
        Intent i = getIntent();

        if (i.hasExtra("name")) { // then the employer is trying to load the employee data
            String name = i.getStringExtra("name");

            titleTxtV.setVisibility(View.GONE);
            editImgBtn.setVisibility(View.INVISIBLE);
            saveBtn.setVisibility(View.INVISIBLE);

            nameInputLyt.setVisibility(View.VISIBLE);
            nameInputEdtTxt.setText(name);
            nameInputEdtTxt.setEnabled(false);

            mobileNbInputEdtTxt.setText(i.getStringExtra("phone"));
            mobileNbInputEdtTxt.setEnabled(false);

            descriptionInputEdtTxt.setText(i.getStringExtra("description"));
            descriptionInputEdtTxt.setEnabled(false);

            loadUserSpinnersData(i.getStringExtra("salary"), i.getStringExtra("position"));
            salariesSpinner.setEnabled(false);
            positionsSpinner.setEnabled(false);

            Picasso.with(getApplicationContext()).load(i.getStringExtra("profilePicture")).placeholder(R.drawable.user_placeholder).into(profilePicImgV);

        } else {
            mLogic.setCallback(this);

            mRef = FirebaseDatabase.getInstance().getReference();
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            showProgressDialog("Loading data...");
            loadData();
            setListeners();
        }
    }

    private void loadData() {
        mobileNbInputEdtTxt.setText(SharedPreferencesManager.getPhoneNb(getApplicationContext()));
        mLogic.getStatus(mRef, getApplicationContext());
    }

    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setTitle("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void setListeners() {
        saveBtn.setOnClickListener(view -> {
            showProgressDialog("Saving new data...");
            mLogic.checkValidation(salariesSpinner.getSelectedItem().toString(), positionsSpinner.getSelectedItem().toString(),
                    mobileNbInputEdtTxt, descriptionInputEdtTxt, mRef, getApplicationContext(), filePath, storageReference);
        });

        signOutImgBtn.setOnClickListener(view -> {
            SharedPreferencesManager.clearSharedPreferences(getApplicationContext());
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        });

        editImgBtn.setOnClickListener(view -> chooseImageFromGallery());
    }

    private void chooseImageFromGallery() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                profilePicImgV.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void loadUserSpinnersData(String salary, String position) {
       int positionIndex =  mLogic.getUserPositionIndex(getApplicationContext(), position);
       int salaryIndex= mLogic.getUserSalaryIndex(getApplicationContext(), salary);

       salariesSpinner.setSelection(salaryIndex);
       positionsSpinner.setSelection(positionIndex);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUserDataRetrieved(User userEmployee) {
        titleTxtV.setText("Welcome, " + userEmployee.getFirstName() + " " + userEmployee.getLastName());
        descriptionInputEdtTxt.setText(userEmployee.getDescription());

        loadUserSpinnersData(userEmployee.getSalary(), userEmployee.getPosition());
        Picasso.with(getApplicationContext()).load(userEmployee.getProfilePicture()).placeholder(R.drawable.user_placeholder).into(profilePicImgV);

        progressDialog.dismiss();
    }

    @Override
    public void onUpdateUserSuccess() {
        if(filePath == null)
            progressDialog.dismiss();

        Toast.makeText(this, "Data updated successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImageUploaded() {
        progressDialog.dismiss();
    }
}