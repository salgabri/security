package com.security.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.security.security.UserRole.*;

@Configuration
@EnableWebSecurity
public class UserConfig {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("STUDENT")
                .password(passwordEncoder.encode("123"))
                .authorities(STUDENT.getAuthorities())
                .build();

        UserDetails userTwo = User.builder()
                .username("ADMIN")
                .password(passwordEncoder.encode("123"))
                .authorities(ADMIN.getAuthorities())
                .build();

        UserDetails userThree = User.builder()
                .username("ADMINTRAINEE")
                .password(passwordEncoder.encode("123"))
                .authorities(ADMINTRAINEE.getAuthorities())
                .build();

        return new InMemoryUserDetailsManager(user, userTwo, userThree);
    }

}
