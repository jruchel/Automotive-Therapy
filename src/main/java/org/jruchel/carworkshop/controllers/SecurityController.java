package org.jruchel.carworkshop.controllers;

import org.jruchel.carworkshop.configuration.Properties;
import org.jruchel.carworkshop.entities.User;
import org.jruchel.carworkshop.exceptions.EntityIntegrityException;
import org.jruchel.carworkshop.services.SecurityService;
import org.jruchel.carworkshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody User user) {
        if (user == null) new ResponseEntity<>(false, HttpStatus.OK);
        return new ResponseEntity<>(securityService.login(user.getUsername(), user.getPassword()), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        securityService.logout();
        return new ResponseEntity<>("Wylogowano", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(HttpServletRequest servletRequest, @RequestBody User user) {
        if (user == null) new ResponseEntity<>(false, HttpStatus.OK);
        if (!verifyAdminKey(servletRequest.getHeader("key")))
            return new ResponseEntity<>("Błędny klucz", HttpStatus.CONFLICT);
        if (userService.loadUserByUsername(user.getUsername()) != null)
            return new ResponseEntity<>("Użytkownik już istnieje", HttpStatus.CONFLICT);
        try {
            securityService.register(user);
            if (userService.loadUserByUsername(user.getUsername()) == null)
                return new ResponseEntity<>("Bład rejestracji, spróbuj ponownie", HttpStatus.OK);
            return new ResponseEntity<>("Rejestracja zakończona.", HttpStatus.OK);
        } catch (EntityIntegrityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    private boolean verifyAdminKey(String key) {
        try {
            String actualKey = Properties.getInstance().readProperty("administrator.key");
            return key.equals(actualKey);
        } catch (Exception e) {
            return false;
        }
    }
}
