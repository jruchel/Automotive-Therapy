package org.jruchel.carworkshop.validation.client;

import org.jruchel.carworkshop.configuration.ApplicationContextHolder;
import org.jruchel.carworkshop.configuration.Properties;
import org.jruchel.carworkshop.validation.Validator;

import java.io.IOException;

/**
 * This class uses manual bean initialization.
 * For some reason Spring is unable to create the bean Properties with annotations
 * and a manual method must be used, I was unable to determine why.
 */
public class EmailValidator extends Validator<EmailConstraint, String> {

    private final Properties properties = ApplicationContextHolder.getContext().getBean(Properties.class);

    protected boolean Constraint_matchesEmailPattern(String value) {
        if (value.isEmpty()) {
            addMessage("Email cannot be empty");
            return false;
        }
        boolean result = value.matches(properties.getEmailPattern());
        if (!result) addMessage("Invalid email.");
        return result;
    }

}
