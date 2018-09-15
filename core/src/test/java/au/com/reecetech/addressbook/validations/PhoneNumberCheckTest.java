package au.com.reecetech.addressbook.validations;

import au.com.reecetech.addressbook.AbstractTest;
import au.com.reecetech.addressbook.models.Phone;
import com.google.i18n.phonenumbers.NumberParseException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class PhoneNumberCheckTest extends AbstractTest {

    @Test
    public void nullValueShouldNotBeValid() {
        PhoneNumberCheck check = new PhoneNumberCheck();

        assertThat("Null should not be valid", !check.isSatisfied(null, null, null, null));
    }

    @Test
    public void phoneShouldBeVerified() throws NumberParseException {
        PhoneNumberCheck check = new PhoneNumberCheck();

        Phone phone1 = new Phone(PHONE_NUMBER_1);
        assertThat("Phone 1 should be valid", check.isSatisfied(null, phone1, null, null));

        Phone phone2 = new Phone(PHONE_NUMBER_INVALID);
        assertThat("Phone 2 should NOT be valid", !check.isSatisfied(null, phone2, null, null));

    }
}

