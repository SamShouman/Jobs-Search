package com.example.jobproject2.Interfaces;

import com.example.jobproject2.Models.User;

public interface IUsersAdapterEvents {
    void onItemClicked(User employee);
    void onEmailImageButtonClicked(User employee);
    void onPhoneImageButtonClicked(User employee);
}
