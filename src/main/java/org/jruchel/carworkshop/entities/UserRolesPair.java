package org.jruchel.carworkshop.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRolesPair {
    private User user;
    private Role[] roles;

    public Set<Role> getRoles() {
        return new HashSet<>(Arrays.asList(roles));
    }
}
