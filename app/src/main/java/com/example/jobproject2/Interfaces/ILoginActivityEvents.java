package com.example.jobproject2.Interfaces;

import java.util.Map;

public interface ILoginActivityEvents {
   void getUserIdByEmail(String email, String password, Map<String, Object> dataSnapshot);
}
