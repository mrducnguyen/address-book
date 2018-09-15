package au.com.reecetech.addressbook;

import au.com.reecetech.addressbook.models.AddressBook;
import au.com.reecetech.addressbook.models.Contact;
import com.google.i18n.phonenumbers.NumberParseException;

public abstract class AbstractTest {
    protected static final String USER_NAME = "Mr Obi-Wan Kenobi";
    protected static final String BOOK_NAME_1 = "Business";
    protected static final String BOOK_NAME_2 = "Personal";
    protected static final String CONTACT_NAME_1 = "Mr. Chewie";
    protected static final String CONTACT_NAME_2 = "Mr. Chewbacca";
    protected static final String CONTACT_NAME_3 = "Mr. Yoda";
    protected static final String CONTACT_NAME_4 = "Mr. Skywalker";
    protected static final String PHONE_NUMBER_1 = "0388885555";
    protected static final String PHONE_NUMBER_2 = "0366665555";
    protected static final String PHONE_NUMBER_3 = "0366667777";
    protected static final String PHONE_NUMBER_4 = "0366669999";
    protected static final String PHONE_NUMBER_INVALID = "0311225";
    protected static final String PHONE_NUMBER_MOBILE = "+61 400 555 666";
    protected static final String PHONE_NUMBER_MOBILE_SAME = "+61400555666";
    protected static final String PHONE_NUMBER_LANDLINE = "0366998888";
    protected static final String PHONE_NUMBER_NOT_NUMBER = "ABCDEFGHIJ";
    protected static final String PHONE_NUMBER_TOO_SHORT = "1";
    protected static String PHONE_NUMBER_TOO_LONG = "";

    static {
        // generate a string of 255 in length
        for (int i = 0; i <= 255; i++) {
            PHONE_NUMBER_TOO_LONG += Math.floor(Math.random()*10); // 0 - 9
        }
    }

    public Contact getContact1() {
        Contact contact = new Contact(CONTACT_NAME_1);
        try {
            contact.addPhone(PHONE_NUMBER_1);
            contact.addPhone(PHONE_NUMBER_2);
        } catch (NumberParseException e) {
            // this code block will never be reached
            // they are here for Java compiler not throwing out error
            e.printStackTrace();
        }

        return contact;
    }

    public Contact getContact2() {
        Contact contact = new Contact(CONTACT_NAME_2);
        try {
            contact.addPhone(PHONE_NUMBER_3);
            contact.addPhone(PHONE_NUMBER_4);
        } catch (NumberParseException e) {
            // this code block will never be reached
            // they are here for Java compiler not throwing out error
            e.printStackTrace();
        }

        return contact;
    }

    public Contact getContact1Duplicate() {
        Contact contact = getContact1();
        contact.setName(CONTACT_NAME_1 + System.currentTimeMillis());
        return contact;
    }

    public Contact getContact1Duplicate2() {
        Contact contact = getContact1();
        contact.getPhones().clear();
        try {
            contact.addPhone(PHONE_NUMBER_2);
            contact.addPhone(PHONE_NUMBER_MOBILE);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        return contact;
    }

    public Contact getContact2Duplicate() {
        Contact contact = getContact1();
        contact.setName(CONTACT_NAME_2);
        return contact;
    }

    public Contact getContact2Duplicate2() {
        Contact contact = getContact1();
        try {
            contact.addPhone(PHONE_NUMBER_4);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        return contact;
    }

    public AddressBook getAddressBookWith2DifferentContacts() {
        AddressBook book = new AddressBook(BOOK_NAME_1);

        book.addContact(getContact1());
        book.addContact(getContact2());

        return book;
    }
}
