package com.upendra.validation;

import java.lang.reflect.Field;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {
    private String[] fieldNames;

    @Override
    public void initialize(AtLeastOneNotNull constraintAnnotation) {
        this.fieldNames = constraintAnnotation.fieldNames();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        for (String fieldName : fieldNames) {
            try {
                Object property = getProperty(value, fieldName);
                if (property != null) {
                    return true;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    private Object getProperty(Object value, String fieldName) throws Exception {
        Field field = value.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(value);
    }
}