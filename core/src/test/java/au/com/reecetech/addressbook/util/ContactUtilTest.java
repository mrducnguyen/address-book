package au.com.reecetech.addressbook.util;

import au.com.reecetech.addressbook.AbstractTest;
import au.com.reecetech.addressbook.models.AddressBook;
import au.com.reecetech.addressbook.models.Contact;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContactUtilTest extends AbstractTest {

    @Test
    public void potentialDuplicatesShouldBeDetectable() {
        AddressBook book1 = getAddressBookWith2DifferentContacts();

        List<Contact> duplicates = ContactUtil.potentialDuplicates(book1.getContacts());
        assertThat("No duplicate should be found", duplicates.size() == 0);

        Contact contact1 = book1.getContacts().get(0);
        Contact contact2 = getContact1Duplicate();
        Contact contact3 = getContact1Duplicate2();
        book1.addContact(contact2);
        book1.addContact(contact3);

        duplicates = ContactUtil.potentialDuplicates(book1.getContacts());

        assertThat("3 duplicates out of 4 should be found, found only: " + duplicates.size(), duplicates.size() == 3);
        assertThat("Contact 1 should be found in duplicates list", duplicates.contains(contact1));
        assertThat("Contact 2 should be found in duplicates list", duplicates.contains(contact2));
        assertThat("Contact 3 should be found in duplicates list", duplicates.contains(contact3));
    }

    @Test
    public void commonContactsEmptyArgumentShouldThrowError() {
        assertThrows(AssertionError.class, () -> ContactUtil.commonContacts());
    }

    @Test
    public void commonContactsShouldBeDetectable() {
        AddressBook book1 = new AddressBook(BOOK_NAME_1);
        AddressBook book2 = new AddressBook(BOOK_NAME_2);
        AddressBook book3 = new AddressBook(BOOK_NAME_1 + System.currentTimeMillis());
        AddressBook book4 = new AddressBook(BOOK_NAME_2 + System.currentTimeMillis());

        Contact contact3 = new Contact(CONTACT_NAME_3);
        Contact contact4 = new Contact(CONTACT_NAME_4);

        Stream.of(book1, book2, book3, book4).forEach(book -> {
            book.addContact(getContact1());
            book.addContact(getContact2());
        });

        Stream.of(book3, book4).forEach(book -> {
            book.addContact(contact3);
            book.addContact(contact4);
        });

        assertThat("No common contact", ContactUtil.commonContacts(book1, new AddressBook("RANDOM")).size() == 0);

        List<Contact> commonContacts = ContactUtil.commonContacts(book1, book2, book3, book4);

        assertThat("There are 2 common contacts", commonContacts.size() == 2);
        assertThat("Contact 1 should be in the common contacts", commonContacts.contains(getContact1()));
        assertThat("Contact 2 should be in the common contacts", commonContacts.contains(getContact2()));

        commonContacts = ContactUtil.commonContacts(book3, book4);

        assertThat("There are 4 common contacts", commonContacts.size() == 4);
        assertThat("Contact 1 should be in the common contacts", commonContacts.contains(getContact1()));
        assertThat("Contact 2 should be in the common contacts", commonContacts.contains(getContact2()));
        assertThat("Contact 3 should be in the common contacts", commonContacts.contains(contact3));
        assertThat("Contact 4 should be in the common contacts", commonContacts.contains(contact4));
    }
}
