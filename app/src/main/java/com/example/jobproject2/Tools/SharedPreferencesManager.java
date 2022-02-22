package com.example.jobproject2.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesManager {
    private static final String PREF_USER_ID = "user_id";
    private static final String PREF_NAME = "name";
    private static final String PREF_PHONE = "phone";
    private static final String PREF_ROLE = "role";
    private static final String PREF_COMPANY_NAME = "company_name";

    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void clearSharedPreferences(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.apply();
    }

    public static void setUserId(Context ctx, String id) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, id);
        editor.apply();
    }

    public static String getUserId(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }

    public static void setRole(Context ctx, String role) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_ROLE, role);
        editor.apply();
    }

    public static String getRole(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_ROLE, "");
    }

    public static void setName(Context ctx, String name) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_NAME, name);
        editor.apply();
    }

    public static String getName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_NAME, "");
    }

    public static void setPhoneNb(Context ctx, String phoneNb) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_PHONE, phoneNb);
        editor.apply();
    }

    public static String getPhoneNb(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_PHONE, "");
    }

    public static void setCompanyName(Context ctx, String companyName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_COMPANY_NAME, companyName);
        editor.apply();
    }

    public static String getCompanyName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_COMPANY_NAME, "");
    }
}
