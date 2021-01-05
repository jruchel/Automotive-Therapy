package org.jruchel.carworkshop.services;

import org.jruchel.carworkshop.entities.User;
import org.jruchel.carworkshop.exceptions.EntityIntegrityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

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
