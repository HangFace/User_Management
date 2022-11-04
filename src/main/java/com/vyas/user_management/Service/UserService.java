package com.vyas.user_management.Service;

import com.vyas.user_management.Entity.User;

public interface UserService {

     User createUser(User user);

     boolean checkEmail(String email);
}
