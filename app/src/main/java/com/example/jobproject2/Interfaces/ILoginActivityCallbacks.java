package com.example.jobproject2.Interfaces;

public interface ILoginActivityCallbacks {
    void onLoginSuccess(String role);
    void onLoginFailure(String exceptionMsg);
}
