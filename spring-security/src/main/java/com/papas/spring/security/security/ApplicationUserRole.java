package com.papas.spring.security.security;

import com.google.common.collect.Sets;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(ApplicationUserPermission.COURSE_WRITE, ApplicationUserPermission.COURSE_READ, ApplicationUserPermission.STUDENT_WRITE, ApplicationUserPermission.STUDENT_READ)),
    ADMINTRAINEE(Sets.newHashSet(ApplicationUserPermission.COURSE_READ, ApplicationUserPermission.STUDENT_READ));


    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {

        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {

        return permissions;
    }


    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
