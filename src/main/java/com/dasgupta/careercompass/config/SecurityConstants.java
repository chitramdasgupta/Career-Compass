package com.dasgupta.careercompass.config;

import java.util.List;

public class SecurityConstants {
    public static final List<String> EXCLUDED_PATHS = List.of("/auth/signup", "/auth/login");
}
