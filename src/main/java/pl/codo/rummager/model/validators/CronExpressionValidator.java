package pl.codo.rummager.model.validators;



import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@ApplicationScoped
public class CronExpressionValidator implements ConstraintValidator<CronExpression, String> {

    @Override
    public boolean isValid(String expression, ConstraintValidatorContext context) {

         return  org.quartz.CronExpression.isValidExpression(expression);


    }
}