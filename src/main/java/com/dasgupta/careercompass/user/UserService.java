package com.dasgupta.careercompass.user;


import java.util.Optional;

public interface UserService {
    Optional<UserDto> getUserByEmail(String email);
}
