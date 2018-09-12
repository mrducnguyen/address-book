package au.com.reecetech.addressbook.models;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ContactTest {

    @Test
    public void emptyContactNameShouldBeInvalid() {
        Validator validator = new Validator();

        Contact empty1 = new Contact(null);
        List<ConstraintViolation> errorList = validator.validate(empty1);

        assertThat(errorList.size(), greaterThan(0));
        ConstraintViolation violation = errorList.get(0);
        assertThat(violation.getMessage(), containsString("cannot be null"));

        Contact empty2 = new Contact("");
        errorList = validator.validate(empty2);

        assertThat(errorList.size(), greaterThan(0));
        violation = errorList.get(0);
        assertThat(violation.getMessage(), containsString("cannot be empty"));
    }
}
