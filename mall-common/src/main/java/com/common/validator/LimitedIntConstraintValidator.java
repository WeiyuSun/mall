package com.common.validator;

import com.common.validator.constraints.LimitedValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author weiyu
 */
public class LimitedIntConstraintValidator implements ConstraintValidator<LimitedValue, Integer> {

    private Set<Integer> valuesSet;

    @Override
    public void initialize(LimitedValue constraintAnnotation) {
        valuesSet = new HashSet<>();
        int[] values = constraintAnnotation.values();

        for(int value: values){
            valuesSet.add(value);
        }
//        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return valuesSet.contains(value);
    }
}
