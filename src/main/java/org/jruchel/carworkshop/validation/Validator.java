package org.whatever.library.validation;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Validator<A extends Annotation, E> implements ConstraintValidator<A, E> {

    protected StringBuilder errorMessage;
    @Autowired
    protected ValidationErrorPasser errorPasser;

    @Override
    public void initialize(A constraintAnnotation) {
        errorMessage = new StringBuilder();
    }

    @Override
    public boolean isValid(E value, ConstraintValidatorContext context) {
        boolean result = true;
        Method[] methods = Arrays.stream(getClass().getMethods()).filter(m -> m.getName().contains("Constraint_")).collect(Collectors.toList()).toArray(new Method[0]);
        for (Method m : methods) {
            try {
                result = result && String.valueOf(m.invoke(this, value)).equals("true");
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        if (errorPasser != null)
            errorPasser.addMessage(value.toString(), errorMessage.toString());
        errorMessage = new StringBuilder();
        return result;
    }

    protected void addMessage(String message) {
        if (!errorMessage.toString().contains(message)) errorMessage.append(message);
    }
}
