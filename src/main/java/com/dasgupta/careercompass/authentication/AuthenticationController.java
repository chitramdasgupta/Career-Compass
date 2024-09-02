package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final Environment env;
    private final AuthResponseUserMapper authResponseUserMapper;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, Environment env,
                                    AuthResponseUserMapper authResponseUserMapper) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.env = env;
        this.authResponseUserMapper = authResponseUserMapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseUserDto> register(@RequestBody AuthRequestUserDto authRequestUserDto) {
        log.info("Register endpoint hit with for user with email: {}", authRequestUserDto.getEmail());
        AuthResponseUserDto registeredUserDto = authenticationService.register(authRequestUserDto);

        log.info("User is registered: {}", registeredUserDto.getEmail());

        return ResponseEntity.ok(registeredUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseUserDto> authenticate(@RequestBody AuthRequestUserDto authRequestUserDto) {
        log.info("Login endpoint called with user email: {}", authRequestUserDto.getEmail());

        User authenticatedUser = authenticationService.authenticate(authRequestUserDto);
        log.info("User is authenticated: {}", authenticatedUser.getEmail());

        ResponseCookie jwtCookie = getJwtCookie(authenticatedUser);
        log.info("Response Cookie with JWT is generated");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(authResponseUserMapper.toDto(authenticatedUser));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        log.info("Logout endpoint called");

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(Arrays.asList(env.getActiveProfiles()).contains("prod"))
                .path("/")
                .maxAge(0)
                .build();

        log.info("JWT cookie cleared");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .build();
    }

    private ResponseCookie getJwtCookie(User authenticatedUser) {
        String jwtToken = jwtService.generateToken(authenticatedUser);
        log.info("JWT token is generated");

        boolean isSecure = Arrays.asList(env.getActiveProfiles()).contains("prod");
        log.info("isSecure: {}", isSecure);

        return ResponseCookie.from("jwt", jwtToken)
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(jwtService.getExpirationTime())
                .build();
    }
}