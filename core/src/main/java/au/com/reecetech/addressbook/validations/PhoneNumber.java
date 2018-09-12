package au.com.reecetech.addressbook.validations;

import net.sf.oval.configuration.annotation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(checkWith = PhoneNumberCheck.class)
public @interface PhoneNumber {
    String message() default "Phone number is invalid";
}
