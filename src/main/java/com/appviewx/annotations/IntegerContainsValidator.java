package com.appviewx.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class IntegerContainsValidator implements ConstraintValidator<IntegerContains,Integer> {
    Integer[] validValues;

    @Override
    public void initialize(IntegerContains constraintAnnotation) {
        int[] validValuesProvided = constraintAnnotation.values();
        validValues = Arrays.stream(validValuesProvided).boxed().toArray(Integer[]::new);
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.asList(validValues).contains(integer);
    }
}
