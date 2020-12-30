package org.jruchel.carworkshop.configuration;

import org.jruchel.carworkshop.automation.Endpoint;
import org.jruchel.carworkshop.automation.SpringSecurityAutomation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] whitelist = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };


    private HttpSecurity setupEndpoints(HttpSecurity http) throws Exception {
        List<Endpoint> endpointList = SpringSecurityAutomation.getAllEndpoints();
        Set<String> roles = getRoles(endpointList);
        roles.removeIf(r -> r.equals(""));
        for (String role : roles) {
            List<Endpoint> roleEndpoints = getEndpointsWithRole(endpointList, role);
            String[] endpointPaths = stringListToArray(roleEndpoints.stream().map(Endpoint::getPath).collect(Collectors.toList()));
            http = http.authorizeRequests().antMatchers(endpointPaths).hasRole(role.toLowerCase()).and();
        }
        List<Endpoint> permitAllEndpoints = getEndpointsWithRole(endpointList, "");
        String[] endpointPaths = stringListToArray(permitAllEndpoints.stream().map(Endpoint::getPath).collect(Collectors.toList()));
        String[] actualPaths = new String[endpointPaths.length + whitelist.length];
        System.arraycopy(whitelist, 0, actualPaths, 0, whitelist.length);
        System.arraycopy(endpointPaths, 0, actualPaths, whitelist.length, actualPaths.length - whitelist.length);
        if (endpointPaths.length > 0)
            return http.authorizeRequests().antMatchers(endpointPaths).permitAll().and();
        else return http;

    }

    private String[] stringListToArray(List<String> list) {
        String[] array = new String[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    private List<Endpoint> getEndpointsWithRole(List<Endpoint> endpoints, String role) {
        List<Endpoint> roleEndpoints = new ArrayList<>();
        for (int i = endpoints.size() - 1; i >= 0; i--) {
            Endpoint endpoint = endpoints.get(i);
            if (endpoint.getRole().equals(role)) {
                roleEndpoints.add(endpoint);
            }
        }
        return roleEndpoints;
    }

    private Set<String> getRoles(List<Endpoint> endpoints) {
        return endpoints.stream().map(Endpoint::getRole).collect(Collectors.toSet());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        setupEndpoints(http).authorizeRequests()
                .anyRequest().authenticated().and()
                .csrf().disable().cors();

    }

}