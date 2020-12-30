package org.jruchel.carworkshop.controllers;

import org.jruchel.carworkshop.automation.Controller;
import org.jruchel.carworkshop.automation.SecuredMapping;
import org.jruchel.carworkshop.automation.SpringSecurityAutomation;
import org.jruchel.carworkshop.automation.Endpoint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController extends Controller {

    @SecuredMapping(path = "/endpoints", method = RequestMethod.GET, role = "moderator")
    public List<Endpoint> getAllEndpoints() {
        SpringSecurityAutomation automation = new SpringSecurityAutomation();
        return automation.getAllEndpoints();
    }

    @SecuredMapping(path = "/principal", method = RequestMethod.GET)
    public String getPrincipal(Principal principal) {

        return principal.getName();
    }

    @SecuredMapping(path = "/authorities", method = RequestMethod.GET)
    public Collection<? extends GrantedAuthority> getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.getAuthorities();
        }
        return new ArrayList<>();
    }
}
