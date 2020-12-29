package org.jruchel.carworkshop.controllers;

import org.jruchel.carworkshop.entities.User;
import org.jruchel.carworkshop.services.SecurityService;
import org.jruchel.carworkshop.services.UserService;
import org.jruchel.carworkshop.utils.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(securityService.login(user.getUsername(), user.getPassword()), HttpStatus.OK);
    }
}
