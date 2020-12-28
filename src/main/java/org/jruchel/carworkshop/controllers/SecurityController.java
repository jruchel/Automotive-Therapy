package org.jruchel.carworkshop.controllers;

import org.jruchel.carworkshop.entities.User;
import org.jruchel.carworkshop.utils.Properties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/security")
public class SecurityController {

    @PostMapping("/logIn")
    public ResponseEntity<Boolean> logIn(@RequestBody User user) {
        return new ResponseEntity<>(validateUser(user), HttpStatus.OK);
    }

    private boolean validateUser(User user) {
        Properties properties = Properties.getInstance();
        try {
            String username = properties.readProperty("moderator.username");
            String password = properties.readProperty("moderator.password");
            return username.equals(user.getUsername()) && password.equals(user.getPassword());
        } catch (IOException e) {
            return false;
        }
    }

}
