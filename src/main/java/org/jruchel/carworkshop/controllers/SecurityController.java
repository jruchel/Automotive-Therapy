package org.jruchel.carworkshop.controllers;

import org.jruchel.carworkshop.automation.Controller;
import org.jruchel.carworkshop.automation.SecuredMapping;
import org.jruchel.carworkshop.configuration.Properties;
import org.jruchel.carworkshop.entities.Role;
import org.jruchel.carworkshop.entities.User;
import org.jruchel.carworkshop.entities.UserRolesPair;
import org.jruchel.carworkshop.exceptions.EntityIntegrityException;
import org.jruchel.carworkshop.services.RoleService;
import org.jruchel.carworkshop.services.SecurityService;
import org.jruchel.carworkshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/security")
public class SecurityController extends Controller {

    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @SecuredMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<Boolean> login(@RequestBody User user) {
        if (user == null) new ResponseEntity<>(false, HttpStatus.OK);
        return new ResponseEntity<>(securityService.login(user.getUsername(), user.getPassword()), HttpStatus.OK);
    }

    @SecuredMapping(path = "/logout", method = RequestMethod.POST, role = "moderator")
    public ResponseEntity<String> logout() {
        securityService.logout();
        return new ResponseEntity<>("Wylogowano", HttpStatus.OK);
    }

    @SecuredMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(HttpServletRequest servletRequest, @RequestBody UserRolesPair userRolesPair) {
        User user = userRolesPair.getUser();
        if (user == null) new ResponseEntity<>(false, HttpStatus.OK);
        if (!verifyAdminKey(servletRequest.getHeader("key")))
            return new ResponseEntity<>("Błędny klucz", HttpStatus.CONFLICT);
        if (userService.loadUserByUsername(user.getUsername()) != null)
            return new ResponseEntity<>("Użytkownik już istnieje", HttpStatus.CONFLICT);
        try {
            Set<Role> roles = userRolesPair.getRoles();
            Set<Role> actualRoles = new HashSet<>();
            for (Role r : roles) {
                Role fromDB = roleService.getRoleByTitle(r.getTitle());
                roles.removeIf(role -> fromDB != null && role.getTitle().equals(fromDB.getTitle()));
                actualRoles.add(fromDB);
            }
            actualRoles.addAll(roles);
            for (Role r : actualRoles) {
                user.grantRole(r);
            }
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
