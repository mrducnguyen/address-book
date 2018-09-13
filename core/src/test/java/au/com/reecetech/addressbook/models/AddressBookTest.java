package au.com.reecetech.addressbook.models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class AddressBookTest extends AbstractModelTest {

    @Test
    public void addressBookShouldHoldContact() {
        AddressBook book = new AddressBook(BOOK_NAME_1);

        Contact contact1 = getContact1();
        Contact contact2 = getContact2();

        book.addContact(contact1);
        book.addContact(contact2);

        assertThat("Contact 1 should be existed", book.hasContact(contact1));
        assertThat("Contact 2 should be existed", book.hasContact(contact2));
    }

    @Test
    public void contactShouldBeRemovable() {
        AddressBook book = new AddressBook(BOOK_NAME_2);

        Contact contact1 = getContact1();
        Contact contact2 = getContact2();

        book.addContact(contact1);
        book.addContact(contact2);

        book.removeContact(contact1);
        assertThat("Contact 1 should be removed", !book.hasContact(contact1));
        book.removeContact(contact2);
        assertThat("Contact 2 should be removed", !book.hasContact(contact2));
    }

    @Test
    public void similarContactShouldBeDetectable() {
        AddressBook book = getAddressBookWith2DifferentContacts();

        Contact dupContact1 = getContact1Duplicate();
        assertThat("Contact should be flagged as similar", book.hasSimilarContact(dupContact1));

        Contact dupContact2 = getContact2Duplicate();
        assertThat("Contact should be flagged as similar", book.hasSimilarContact(dupContact2));

        Contact noDupContact = new Contact(CONTACT_NAME_1 + System.currentTimeMillis());
        assertThat("Contact should not be similar", !book.hasSimilarContact(noDupContact));
    }

    @Test
    public void findContactByNameShouldWork() {
        AddressBook book = getAddressBookWith2DifferentContacts();

        List<Contact> list = book.findContactByName(CONTACT_NAME_1);
        assertThat("Contact '" + CONTACT_NAME_1 + "' should be found", list.size() > 0);

        list = book.findContactByName(CONTACT_NAME_1.substring(3), true);
        assertThat("Partial name should be found in lenient search", list.size() > 0);
    }

    @Test
    public void findIntersectionsShouldWork() {
        AddressBook book1 = getAddressBookWith2DifferentContacts();
        AddressBook book2 = getAddressBookWith2DifferentContacts();

        Contact contact = book2.findContactByName(CONTACT_NAME_2).get(0);
        book2.removeContact(contact);
        book2.addContact(getContact1Duplicate());
        book2.addContact(getContact1Duplicate2());

        Contact contact1 = book1.findContactByName(CONTACT_NAME_1).get(0);

        List<Contact> intersections = book1.intersectionContacts(book2);
        assertThat("Should have 1 intersection contact", intersections.size() == 1);
        assertThat("Contact 1 should be the intersection", intersections.contains(contact1));
    }

    @Test
    public void mergingBookShouldWork() {
        AddressBook book1 = getAddressBookWith2DifferentContacts();
        AddressBook book2 = new AddressBook(BOOK_NAME_2);
        book2.addContact(new Contact(CONTACT_NAME_3));
        book2.addContact(new Contact(CONTACT_NAME_4));

        book1.mergeContacts(book2);

        assertThat("Book 1 should have 4 contacts", book1.getContacts().size() == 4);
        assertThat("Contact '" + CONTACT_NAME_1 + "' should be existed",
            book1.findContactByName(CONTACT_NAME_1).size() == 1);
        assertThat("Contact '" + CONTACT_NAME_2 + "' should be existed",
            book1.findContactByName(CONTACT_NAME_2).size() == 1);
        assertThat("Contact '" + CONTACT_NAME_3 + "' should be existed",
            book1.findContactByName(CONTACT_NAME_3).size() == 1);
        assertThat("Contact '" +CONTACT_NAME_4 + "' should be existed",
            book1.findContactByName(CONTACT_NAME_4).size() == 1);
    }

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
