package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @EnableWebSecurity
    public class SecuritySha {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new ShaPassword();
        }

        @Bean
        public InMemoryUserDetailsManager userDetailsManager() {
            // Using the custom password encoder
            UserDetails user = User.builder()
                    .username("user")
                    .password(passwordEncoder().encode("password"))
                    .roles("TEXT")
                    .build();
            return new InMemoryUserDetailsManager(user);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> {
                        auth.requestMatchers("/", "/test", "/getUser", "/create","/logInUser").permitAll(); // Allow access to these endpoints without authentication
                        auth.anyRequest().authenticated();
                    })
                    .httpBasic(Customizer.withDefaults())
                    .build();
        }
    }