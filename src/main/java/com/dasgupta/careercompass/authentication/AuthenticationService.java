package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.User;
import org.springframework.stereotype.Service;

public interface AuthenticationService {
    AuthResponseUserDto register(AuthRequestUserDto input);

    User authenticate(AuthRequestUserDto input);
}
