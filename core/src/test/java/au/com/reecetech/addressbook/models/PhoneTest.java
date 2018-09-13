package au.com.reecetech.addressbook.models;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PhoneTest extends AbstractModelTest {

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
    public void invalidPhoneShouldThrowExceptions() {
        try {
            new Phone("");
        } catch (NumberParseException e) {
            assertThat(e.getErrorType(), is(NumberParseException.ErrorType.NOT_A_NUMBER));
        }

        try {
            new Phone(PHONE_NUMBER_NOT_NUMBER);
        } catch (NumberParseException e) {
            assertThat(e.getErrorType(), is(NumberParseException.ErrorType.NOT_A_NUMBER));
        }

        try {
            new Phone(PHONE_NUMBER_NOT_VALID_COUNTRY);
        } catch (NumberParseException e) {
            assertThat(e.getErrorType(), is(NumberParseException.ErrorType.INVALID_COUNTRY_CODE));
        }

        try {
            new Phone(PHONE_NUMBER_TOO_SHORT);
        } catch (NumberParseException e) {
            assertThat(e.getErrorType(), is(NumberParseException.ErrorType.TOO_SHORT_AFTER_IDD));
        }

        try {
            new Phone(PHONE_NUMBER_TOO_LONG);
        } catch (NumberParseException e) {
            assertThat(e.getErrorType(), is(NumberParseException.ErrorType.TOO_LONG));
        }
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
