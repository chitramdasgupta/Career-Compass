package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.LoginUserDto;
import com.dasgupta.careercompass.user.RegisterUserDto;
import com.dasgupta.careercompass.user.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    User register(RegisterUserDto input);

    User authenticate(LoginUserDto input);
}
