package org.example.user.entities;

import java.security.Permission;
import java.util.Collections;
import java.util.Set;

public enum Role {
    ADMIN(Collections.emptySet()),
    MODERATOR(Collections.emptySet()),
    READER(Collections.emptySet());

    private final Set<Permission> permissions;


    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
