package au.com.reecetech.addressbook.models;

import au.com.reecetech.addressbook.AbstractTest;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PhoneTest extends AbstractTest {

    @Test
    public void nullNumberShouldBeThrown() {
        assertThrows(NullPointerException.class, () -> new Phone(null));

        assertThrows(NullPointerException.class, () -> new Phone(PHONE_NUMBER_1).setNumber(null));
    }

    @Test
    public void mobilePhoneShouldBeSetCorrectly() throws NumberParseException {
        Phone phone = new Phone(PHONE_NUMBER_MOBILE);

        assertThat(phone.toString(), equalTo(PHONE_NUMBER_MOBILE));
        assertThat(phone.getType(), is(PhoneNumberUtil.PhoneNumberType.MOBILE));
    }

    @Test
    public void landlinePhoneShouldBeSetCorrectly() throws NumberParseException {
        Phone phone = new Phone(PHONE_NUMBER_LANDLINE);
        assertThat(phone.getNumber().getRawInput(), equalTo(PHONE_NUMBER_LANDLINE));
        assertThat(phone.getType(), is(PhoneNumberUtil.PhoneNumberType.FIXED_LINE));
    }

    @Test
    public void invalidPhoneShouldThrowExceptions() throws NumberParseException {
        assertThrows(NumberParseException.class, () -> new Phone(""));
        assertThrows(NumberParseException.class, () -> new Phone(PHONE_NUMBER_NOT_NUMBER));
        assertThrows(NumberParseException.class, () -> new Phone(PHONE_NUMBER_TOO_SHORT));
        assertThrows(NumberParseException.class, () -> new Phone(PHONE_NUMBER_TOO_LONG));
    }

    @Test
    public void phoneEqualsDetection() throws NumberParseException {
        Phone phone1 = new Phone(PHONE_NUMBER_MOBILE);
        Phone phone2 = new Phone(PHONE_NUMBER_MOBILE_SAME);
        Phone phone3 = new Phone(PHONE_NUMBER_LANDLINE);

        assertThat("Phone equals object", phone1.equals(phone2));
        assertThat("Phone should not equal object", !phone1.equals(phone3));
        assertThat(format("Phone equals string '%s' should be true", PHONE_NUMBER_MOBILE),
            phone1.equals(PHONE_NUMBER_MOBILE));
        assertThat(format("Phone equals string '%s'should be true", PHONE_NUMBER_MOBILE_SAME),
            phone1.equals(PHONE_NUMBER_MOBILE_SAME));
        assertThat(format("Phone should not equal string '%s'", PHONE_NUMBER_LANDLINE),
            !phone1.equals(PHONE_NUMBER_LANDLINE));
        assertThat("Phone should not equal null", !phone1.equals(null));
        assertThat("Invalid phone should not equal", !phone1.equals(PHONE_NUMBER_NOT_NUMBER));
    }
}
