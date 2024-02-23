package eventy.controllers.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = OfLegalAgeValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
public @interface OfLegalAge {
    String message() default "{eventy.controllers.constraints.OfLegalAge.message}";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};
}
