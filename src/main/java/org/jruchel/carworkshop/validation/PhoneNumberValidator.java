package org.jruchel.carworkshop.validation;

import org.jruchel.carworkshop.utils.Properties;

import java.io.IOException;

public class PhoneNumberValidator extends org.whatever.library.validation.Validator<PhoneNumberConstraint, String> {

    private Properties properties = Properties.getInstance();

    public boolean Constraint_matchesPhonePattern(String value) {
        try {
            return value.matches(properties.readProperty("pattern.phone"));
        } catch (IOException e) {
            return false;
        }
    }

}
