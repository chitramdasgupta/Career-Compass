package com.dasgupta.careercompass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CareerCompassApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareerCompassApplication.class, args);
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(@NonNull CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("*")
//                        .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");
//            }
//        };
//    }

}
