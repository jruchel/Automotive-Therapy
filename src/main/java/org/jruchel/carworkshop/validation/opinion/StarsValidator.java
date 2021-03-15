package org.jruchel.carworkshop.validation.opinion;

import org.jruchel.carworkshop.validation.Validator;

public class StarsValidator extends Validator<StarsConstraint, Integer> {

    protected boolean Constraint_starsWithinRange(int value) {
        boolean result = value > 0 && value <= 5;
        if (!result) addMessage("Ilość gwiazdek musi być pomiędzy 0 a 5.");
        return result;
    }

}
