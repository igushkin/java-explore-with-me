package ru.practicum.ewm.base.util.notblanknull;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotBlankNullConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankNull {

    String message() default "Если поле не содержит null, то оно не должно быть пустым";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
