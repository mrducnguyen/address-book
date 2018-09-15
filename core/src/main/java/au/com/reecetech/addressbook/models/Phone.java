package au.com.reecetech.addressbook.models;

import au.com.reecetech.addressbook.validations.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.NonNull;
import net.sf.oval.constraint.NotEmpty;

public class Phone {

    private static final String DEFAULT_COUNTRY_CODE = "AU";
    private static final PhoneNumberUtil PHONE_UTIL = PhoneNumberUtil.getInstance();
    private static final PhoneNumberUtil.PhoneNumberFormat DEFAULT_FORMAT = PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL;

    @NotEmpty
    @PhoneNumber
    private Phonenumber.PhoneNumber number;

    private PhoneNumberUtil.PhoneNumberType type;

    public Phone(@NonNull String number) throws NumberParseException {
        this.setNumber(number);
    }

    public String getNumber(PhoneNumberUtil.PhoneNumberFormat format) {
        return PHONE_UTIL.format(this.number, format);
    }

    public void setNumber(@NonNull String number) throws NumberParseException {
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

    public boolean isPhoneNumberSame(String number) {
        try {
            Phone phone = new Phone(number);
            return this.isPhoneNumberSame(phone);
        } catch (NumberParseException e) {
            // we should log the exception for debugging purpose
            // there is no logging configured at the moment
            // so this catch block was left empty
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getNumber(DEFAULT_FORMAT);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Phone) {
            return this.isPhoneNumberSame((Phone)other);
        } else if (other instanceof String) {
            return this.isPhoneNumberSame(other.toString());
        }
        return false;
    }
}
