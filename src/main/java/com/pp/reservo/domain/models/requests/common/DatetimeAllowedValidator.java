package com.pp.reservo.domain.models.requests.common;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DatetimeAllowedValidator implements ConstraintValidator<DatetimeAllowed, String> {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void initialize(DatetimeAllowed constraintAnnotation) {

    }

    @Override
    public boolean isValid(String requestedDate, ConstraintValidatorContext constraintValidatorContext) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try
        {
            sdf.setLenient(false);
            sdf.parse(requestedDate);
            return true;
        }
        catch (ParseException e)
        {
            return false;
        }
    }
}
