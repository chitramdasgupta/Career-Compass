package com.dasgupta.careercompass.user;


public interface UserService {
    UserDto getUserByEmail(String email);

    UserDto createUser(String email, String password, Role role, String name);
}
