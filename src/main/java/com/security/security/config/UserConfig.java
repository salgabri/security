package com.security.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.security.security.UserPermission.COURSE_WRITE;
import static com.security.security.UserPermission.STUDENT_WRITE;
import static com.security.security.UserRole.*;

@Configuration
@EnableWebSecurity
public class UserConfig extends WebSecurityConfigurerAdapter {

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

    @Order(2)
    @Configuration
    public static class FormLoginSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/page-api/**")
                    .csrf()
                    .and()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login");
        }
    }

    @Order(1)
    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class BasicHttpSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http

                    .antMatcher("/api/**")
                    //.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/v1/students/**").hasRole(STUDENT.name())
                    .and()
                    .authorizeRequests()
//                    .antMatchers(HttpMethod.GET, "/api/v1/management/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
                    .antMatchers(HttpMethod.DELETE, "/api/v1/management/**").hasAnyAuthority(STUDENT_WRITE.getPermission(), COURSE_WRITE.getPermission())
                    .antMatchers(HttpMethod.POST, "/api/v1/management/**").hasAnyAuthority(STUDENT_WRITE.getPermission(), COURSE_WRITE.getPermission())
                    .antMatchers(HttpMethod.PUT, "/api/v1/management/**").hasAnyAuthority(STUDENT_WRITE.getPermission(), COURSE_WRITE.getPermission())
                    .and()
                    .httpBasic();
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/**/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.xlsx"
                )
        ;
    }


}
