package au.com.reecetech.addressbook.validations;

import au.com.reecetech.addressbook.models.Phone;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

public class PhoneNumberCheck extends AbstractAnnotationCheck<PhoneNumber> {

    private static final String DEFAULT_COUNTRY = "AU";

    @Override
    public boolean isSatisfied(Object validatedObject, Object valueToValidate, OValContext context, Validator validator) throws OValException {
        if (valueToValidate != null) {
            Phone phone = (Phone)valueToValidate;
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            return phoneUtil.isValidNumber(phone.getNumber());
        }
        return false;
    }
}
