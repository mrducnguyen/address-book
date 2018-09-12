package au.com.reecetech.addressbook.models;

import au.com.reecetech.addressbook.validations.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

public class Phone {

    private static final String DEFAULT_COUNTRY_CODE = "AU";
    private static final PhoneNumberUtil PHONE_UTIL = PhoneNumberUtil.getInstance();
    private static final PhoneNumberUtil.PhoneNumberFormat DEFAULT_FORMAT = PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL;

    @NotNull
    @NotEmpty
    @PhoneNumber
    private Phonenumber.PhoneNumber number;

    private PhoneNumberUtil.PhoneNumberType type;

    public Phone(String number) throws NumberParseException {
        this.setNumber(number);
    }

    public String getNumber(PhoneNumberUtil.PhoneNumberFormat format) {
        return PHONE_UTIL.format(this.number, format);
    }

    public void setNumber(String number) throws NumberParseException {
        this.number = PHONE_UTIL.parseAndKeepRawInput(number, DEFAULT_COUNTRY_CODE);
        this.type = PHONE_UTIL.getNumberType(this.number);
    }

    public final Phonenumber.PhoneNumber getNumber() {
        return this.number;
    }

    public final PhoneNumberUtil.PhoneNumberType getType() {
        return this.type;
    }

    public boolean isPhoneNumberSame(Phone other) {
        return this.toString().equals(other.toString());
    }

    @Override
    public String toString() {
        return this.getNumber(DEFAULT_FORMAT);
    }
}
