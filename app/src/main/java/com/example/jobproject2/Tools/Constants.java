package com.example.jobproject2.Tools;

public class Constants {

    public static final String ROLE_EMPLOYEE = "Employee";
    public static final String ROLE_EMPLOYER = "Employer";

    public static final String FIREBASE_REF_USERS = "users";
    public static final String FIREBASE_REF_FAV_USERS = "favourite_users";
    public static final String FIREBASE_REF_POSITIONS = "positions";
    public static final String FIREBASE_REF_SALARIES = "salaries";

    public static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //First name must start with letter A-Z, followed by at least 2 letters a-z
    // Optionally, there can be names following the first name, separated by a space and beginning with letter A-Z, and followed by optional letters a-z
    public static final String NAME_PATTERN = "[a-zA-Z]+";

    public static final String EMAIL_SENDER = "mal3ab.app@gmail.com";
    public static final String EMAIL_PASSWORD = "chouman789";
}
