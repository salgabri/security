package com.security.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.security.security.UserPermission.*;

public enum UserRole {
    STUDENT(Sets.newHashSet()),
    ADMINTRAINEE(Sets.newHashSet(
            COURSE_READ,
            STUDENT_READ)),
    ADMIN(Sets.newHashSet(
            COURSE_READ,
            COURSE_WRITE,
            STUDENT_READ,
            STUDENT_WRITE));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
      Set<SimpleGrantedAuthority> authorities =  getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
      authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
      return authorities;
    }
}
