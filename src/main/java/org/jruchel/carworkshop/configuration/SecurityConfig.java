package org.jruchel.carworkshop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends MySecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    static {
        setWhitelist(Arrays.asList("/swagger-resources/**",
                "/v2/api-docs",
                "/webjars/**"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        setupEndpoints(http).authorizeRequests()
                .antMatchers("swagger-ui.html").hasRole("MODERATOR")
                .anyRequest().authenticated().and()
                .csrf().disable().cors();
    }

}