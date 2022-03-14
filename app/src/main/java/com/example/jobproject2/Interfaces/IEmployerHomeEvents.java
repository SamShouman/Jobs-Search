package com.example.jobproject2.Interfaces;

import java.util.Map;

public interface IEmployerHomeEvents {
    void collectAllUsers(Map<String, Object> map, IEmployeeHomeFirebaseCallback callback);
    void collectFavouriteUsers(Map<String, Object> map, Map<String, String> favUserIdsMap, IEmployeeHomeFirebaseCallback callback);
}
