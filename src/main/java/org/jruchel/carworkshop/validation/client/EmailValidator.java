package org.jruchel.carworkshop.validation.client;

import org.jruchel.carworkshop.utils.Properties;
import org.jruchel.carworkshop.validation.Validator;

import java.io.IOException;

public class EmailValidator extends Validator<EmailConstraint, String> {

    private final Properties properties = Properties.getInstance();

    protected boolean Constraint_matchesEmailPattern(String value) {
        try {
            boolean result = value.matches(properties.readProperty("pattern.email"));
            if(!result) addMessage("Invalid email.");
            return result;
        } catch (IOException e) {
            addMessage("Internal validation error.");
            return false;
        }
    }

}
