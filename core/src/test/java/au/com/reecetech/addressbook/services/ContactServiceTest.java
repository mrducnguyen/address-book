package au.com.reecetech.addressbook.services;

import au.com.reecetech.addressbook.models.AddressBook;
import au.com.reecetech.addressbook.models.Contact;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class ContactServiceTest {

    @Test
    public void potentialDuplicatesShouldBeDetected() {
        AddressBook book1 = getAddressBookWith2DifferentContacts();

        List<Contact> duplicates = book1.potentialDuplicates();
        assertThat("No duplicate should be found", duplicates.size() == 0);

        Contact contact1 = book1.getContacts().get(0);
        Contact contact2 = getContact1Duplicate();
        Contact contact3 = getContact1Duplicate2();
        book1.addContact(contact2);
        book1.addContact(contact3);

        duplicates = book1.potentialDuplicates();

        assertThat("3 duplicates out of 4 should be found, found only: " + duplicates.size(), duplicates.size() == 3);
        assertThat("Contact 1 should be found in duplicates list", duplicates.contains(contact1));
        assertThat("Contact 2 should be found in duplicates list", duplicates.contains(contact2));
        assertThat("Contact 3 should be found in duplicates list", duplicates.contains(contact3));
    }
}
