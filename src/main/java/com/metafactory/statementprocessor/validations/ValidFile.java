package com.metafactory.statementprocessor.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = FileValidator.class)
@Documented
public @interface ValidFile {
    String message() default "Only XML and CSV files are allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
