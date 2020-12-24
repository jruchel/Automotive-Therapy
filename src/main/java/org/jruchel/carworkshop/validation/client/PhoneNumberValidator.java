package org.jruchel.carworkshop.validation.client;

import org.jruchel.carworkshop.utils.Properties;
import org.jruchel.carworkshop.validation.Validator;

import java.io.IOException;

public class PhoneNumberValidator extends Validator<PhoneNumberConstraint, String> {

    private Properties properties = Properties.getInstance();

    protected boolean Constraint_matchesPhonePattern(String value) {
        try {
            if (value.isEmpty()) return true;
            boolean result = value.matches(properties.readProperty("pattern.phone"));
            if (!result) addMessage("Invalid phone number.");
            return result;
        } catch (IOException e) {
            addMessage("Internal validation error.");
            return false;
        }
    }

}
