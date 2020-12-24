package org.jruchel.carworkshop.validation.client;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailConstraint {
    String message() default "Name is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}