package org.jruchel.carworkshop.validation.client;

import org.jruchel.carworkshop.configuration.ApplicationContextHolder;
import org.jruchel.carworkshop.configuration.Properties;
import org.jruchel.carworkshop.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * This class uses manual bean initialization.
 * For some reason Spring is unable to create the bean Properties to use in this class with annotations
 * and a manual method must be used, I was unable to determine why.
 */
public class PhoneNumberValidator extends Validator<PhoneNumberConstraint, String> {

    private final Properties properties = ApplicationContextHolder.getContext().getBean(Properties.class);

    protected boolean Constraint_matchesPhonePattern(String value) {
        if (value.isEmpty()) return true;
        boolean result = value.matches(properties.getPhonePattern());
        if (!result) addMessage("Invalid phone number.");
        return result;

    }

}
