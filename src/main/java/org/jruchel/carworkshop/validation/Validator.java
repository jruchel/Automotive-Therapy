package org.jruchel.carworkshop.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Validator<A extends Annotation, E> implements ConstraintValidator<A, E> {

    protected ValidationErrorPasser errorPasser;
    protected StringBuilder errorMessage;

    @Override
    public void initialize(A constraintAnnotation) {
        errorMessage = new StringBuilder();
        errorPasser = ValidationErrorPasser.getInstance();
    }

    @Override
    public boolean isValid(E value, ConstraintValidatorContext context) {
        boolean result = true;
        Method[] methods = Arrays.stream(getClass().getDeclaredMethods()).filter(m -> m.getName().contains("Constraint_")).toArray(Method[]::new);
        for (Method m : methods) {
            try {
                m.setAccessible(true);
                result = result && String.valueOf(m.invoke(this, value)).equals("true");
                m.setAccessible(false);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return false;
            }

        }
        if (errorPasser != null) {
            try {
                errorPasser.addMessage(value.toString(), errorMessage.toString());
            } catch (Exception ex) {
                errorPasser.addMessage("Unknown parameter", "Null value.");
            }
        }
        errorMessage = new StringBuilder();
        return result;
    }

    protected void addMessage(String message) {
        if (!errorMessage.toString().contains(message)) errorMessage.append(message);
    }
}
