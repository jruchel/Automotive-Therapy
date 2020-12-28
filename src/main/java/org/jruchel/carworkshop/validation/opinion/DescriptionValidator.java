package org.jruchel.carworkshop.validation.opinion;
import org.jruchel.carworkshop.validation.Validator;

public class DescriptionValidator extends Validator<DescriptionConstraint, String> {

    protected boolean Constraint_notEmpty(String value) {
        boolean result = value != null && !value.isEmpty();
        if (!result) addMessage("Opis nie może być pusty");
        return result;
    }

    protected boolean Constraint_withinLimit(String value) {
        boolean result = value.length() <= 200;
        if (!result) addMessage("Opis może mieć co najwyżej 200 znaków");
        return result;
    }

}
