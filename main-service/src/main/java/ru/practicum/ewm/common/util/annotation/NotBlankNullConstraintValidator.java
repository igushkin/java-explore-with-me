package ru.practicum.ewm.common.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankNullConstraintValidator implements ConstraintValidator<NotBlankNull, String> {

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        if (text == null) {
            return true;
        } else {
            return !text.isBlank();
        }
    }
}