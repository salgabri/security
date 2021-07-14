package com.security.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.security.security.UserPermission.COURSE_WRITE;
import static com.security.security.UserPermission.STUDENT_WRITE;
import static com.security.security.UserRole.STUDENT;

@Configuration
@Order(1)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BasicHttpSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("swagger-ui.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*")
                .permitAll()
                .and()
                    .authorizeRequests()
                    .antMatchers("/api/v1/students/**").hasRole(STUDENT.name())
                .and()
                    .authorizeRequests()
//                    .antMatchers(HttpMethod.GET, "/api/v1/management/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
                    .antMatchers(HttpMethod.DELETE, "/api/v1/management/**").hasAnyAuthority(STUDENT_WRITE.getPermission(), COURSE_WRITE.getPermission())
                    .antMatchers(HttpMethod.POST, "/api/v1/management/**").hasAnyAuthority(STUDENT_WRITE.getPermission(), COURSE_WRITE.getPermission())
                    .antMatchers(HttpMethod.PUT, "/api/v1/management/**").hasAnyAuthority(STUDENT_WRITE.getPermission(), COURSE_WRITE.getPermission())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

}
