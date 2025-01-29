package com.appviewx.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IntegerContainsValidator.class)
public @interface IntegerContains {
    int[] values();
    public String message() default "Invalid Integer provided";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
