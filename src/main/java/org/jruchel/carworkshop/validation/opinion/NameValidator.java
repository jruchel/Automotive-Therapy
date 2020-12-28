package org.jruchel.carworkshop.validation.opinion;
import org.jruchel.carworkshop.validation.Validator;

public class NameValidator extends Validator<NameConstraint, String> {

    protected boolean Constraint_withinLimit(String value) {
        boolean result = value.length() < 10;
        if (!result) addMessage("Nazwa nie może być dłuższa niż 10 znaków");
        return result;
    }

}
