package org.jruchel.carworkshop.validation.order;

import org.jruchel.carworkshop.validation.client.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ClientValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientConstraint {
    String message() default "Name is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}