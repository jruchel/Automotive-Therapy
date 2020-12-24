package org.jruchel.carworkshop.validation;

import org.jruchel.carworkshop.utils.Properties;

import java.io.IOException;

public class EmailValidator extends org.whatever.library.validation.Validator<EmailConstraint, String> {

    private Properties properties = Properties.getInstance();

    public boolean Constraint_matchesEmailPattern(String value) {
        try {
            return value.matches(properties.readProperty("pattern.email"));
        } catch (IOException e) {
            return false;
        }
    }

}
