package pesko.orgasms.app.utils;

import javax.validation.Validation;
import javax.validation.Validator;

public class ValidatorUtilImpl implements ValidatorUtil {
    @Override
    public <E> boolean isValid(E entity) {

        Validator validator= Validation.buildDefaultValidatorFactory().getValidator();

        return validator.validate(entity).size()<1;
    }
}
