package au.com.reecetech.addressbook.models;

import au.com.reecetech.addressbook.AbstractTest;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddressBookTest extends AbstractTest {

    @Test
    public void nullArgumentsShouldBeThrown() {
        assertThrows(NullPointerException.class, () -> new AddressBook(null));

        assertThrows(NullPointerException.class, () -> new AddressBook(BOOK_NAME_1).setName(null));
    }

    @Test
    public void emptyNameShouldBeInvalid() {
        Validator validator = new Validator();

        AddressBook empty2 = new AddressBook("");
        List<ConstraintViolation> errorList = validator.validate(empty2);

        assertThat(errorList.size(), greaterThan(0));
        ConstraintViolation violation = errorList.get(0);
        assertThat(violation.getMessage(), containsString("cannot be empty"));
    }

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

        Contact noDupContact = new Contact(format("%s-%d", CONTACT_NAME_1, System.currentTimeMillis()));
        assertThat("Contact should not be similar", !book.hasSimilarContact(noDupContact));
    }

    @Test
    public void findContactByNameShouldWork() {
        assertThrows(NullPointerException.class, () -> new AddressBook(BOOK_NAME_1).findContactByName(null));

        assertThrows(NullPointerException.class, () -> new AddressBook(BOOK_NAME_1).findContactByName(null, true));

        AddressBook book = getAddressBookWith2DifferentContacts();

        List<Contact> list = book.findContactByName(CONTACT_NAME_1);
        assertThat(format("Contact '%s' should be found", CONTACT_NAME_1), list.size() > 0);

        list = book.findContactByName(CONTACT_NAME_1.substring(3), true);
        assertThat(format("Partial name '%s' should be found in lenient search", CONTACT_NAME_1.substring(3)),
            list.size() > 0);
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
        assertThat(format("Contact '%s' should be existed", CONTACT_NAME_1),
            book1.findContactByName(CONTACT_NAME_1).size() == 1);
        assertThat(format("Contact '%s' should be existed", CONTACT_NAME_2),
            book1.findContactByName(CONTACT_NAME_2).size() == 1);
        assertThat(format("Contact '%s' should be existed", CONTACT_NAME_3),
            book1.findContactByName(CONTACT_NAME_3).size() == 1);
        assertThat(format("Contact '%s' should be existed", CONTACT_NAME_4),
            book1.findContactByName(CONTACT_NAME_4).size() == 1);
    }
}
