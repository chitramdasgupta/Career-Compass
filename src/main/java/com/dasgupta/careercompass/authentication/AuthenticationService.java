package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.User;
import com.dasgupta.careercompass.user.UserAuthDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    User register(UserAuthDto input);

    User authenticate(UserAuthDto input);
}
