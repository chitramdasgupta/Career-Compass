package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.User;
import com.dasgupta.careercompass.user.UserAuthDto;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final Environment env;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, Environment env) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.env = env;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody UserAuthDto userAuthDto) {
        User registeredUser = authenticationService.register(userAuthDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody UserAuthDto userAuthDto) {
        User authenticatedUser = authenticationService.authenticate(userAuthDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        boolean isSecure = Arrays.asList(env.getActiveProfiles()).contains("prod");

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwtToken)
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(jwtService.getExpirationTime())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body("Login successful");
    }
}