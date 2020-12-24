package org.jruchel.carworkshop.validation.order;

import org.jruchel.carworkshop.validation.Validator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateValidator extends Validator<DateConstraint, Date> {


    protected boolean Constraint_dateNotNull(Date value) {
        boolean result = value != null;
        if(!result) addMessage("Date must not be empty");
        return result;
    }

    protected boolean Constraint_dateNotFromFuture(Date value) {
        boolean result = value.before(new Date());
        if (!result) addMessage("Date cannot be from the future");
        return result;
    }

    protected boolean Constraint_dateNotOlderThan(Date value) {
        Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant valueInstant = value.toInstant();
        boolean result = yesterday.isBefore(valueInstant);
        if (!result) addMessage("Date cannot be older than a day");
        return result;
    }
}
