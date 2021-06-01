package com.pp.reservo.domain.models.requests.common;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DatetimeAllowedValidator.class)
public @interface DatetimeAllowed {
    String message() default "Invalid datetime format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
