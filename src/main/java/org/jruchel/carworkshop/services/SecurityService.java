package org.jruchel.carworkshop.services;

import org.jruchel.carworkshop.models.entities.User;
import org.jruchel.carworkshop.exceptions.EntityIntegrityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class SecurityService {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean authenticate(String username, String password) {
        UserDetails fromDB = userService.loadUserByUsername(username);
        if (fromDB == null) return false;
        if (!fromDB.getUsername().equals(username)) return false;
        if (!passwordEncoder.matches(password, fromDB.getPassword())) return false;
        return true;
    }

    public boolean register(User user) throws EntityIntegrityException {
        if (userService.loadUserByUsername(user.getUsername()) != null) return false;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return true;
    }
}
