package eventy.controllers.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

public class OfLegalAgeValidator implements ConstraintValidator<OfLegalAge, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthdate, today);

        return period.getYears() >= 18;
    }
}
