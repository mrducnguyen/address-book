package au.com.reecetech.addressbook.models;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PhoneTest {

    private static final String PHONE_NUMBER_MOBILE = "+61 400 555 666";
    private static final String PHONE_NUMBER_MOBILE_SAME = "+61400555666";
    private static final String PHONE_NUMBER_LANDLINE = "0366998888";
    private static final String PHONE_NUMBER_NOT_NUMBER = "ABCDEFGHIJ";
    private static final String PHONE_NUMBER_NOT_VALID_COUNTRY = "12";
    private static final String PHONE_NUMBER_TOO_SHORT = "12345";
    private static final String PHONE_NUMBER_TOO_LONG = "1234567896546897";

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
        assertThat("Phone equals string '" + PHONE_NUMBER_MOBILE + "' should be true", phone1.equals(PHONE_NUMBER_MOBILE));
        assertThat("Phone equals string '" + PHONE_NUMBER_MOBILE_SAME + "'should be true", phone1.equals(PHONE_NUMBER_MOBILE_SAME));
        assertThat("Phone should not equal string '"+ PHONE_NUMBER_LANDLINE + "'", !phone1.equals(PHONE_NUMBER_LANDLINE));
        assertThat("Phone should not equal null", !phone1.equals(null));
        assertThat("Invalid phone should not equal", !phone1.equals(PHONE_NUMBER_NOT_NUMBER));
    }
}
