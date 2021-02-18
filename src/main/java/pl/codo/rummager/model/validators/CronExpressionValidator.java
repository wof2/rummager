package pl.codo.rummager.model.validators;



import pl.codo.rummager.model.MonitoringMetric;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@ApplicationScoped
//FIXME: does not work
public class CronExpressionValidator implements ConstraintValidator<CronExpression, String> {

    @Override
    public boolean isValid(String expression, ConstraintValidatorContext context) {

         return  org.quartz.CronExpression.isValidExpression(expression);


    }
}