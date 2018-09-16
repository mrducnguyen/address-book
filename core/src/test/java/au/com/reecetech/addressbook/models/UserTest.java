package au.com.reecetech.addressbook.models;

import au.com.reecetech.addressbook.AbstractTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest extends AbstractTest {

    private User user;

    @BeforeEach
    public void createAUser() {
        this.user = new User(USER_NAME);
    }

    @Test
    public void userNameShouldBeSet() {
        assertThrows(NullPointerException.class, () -> new User(null));

        assertThrows(NullPointerException.class, () -> user.setUsername(null));

        String newName = USER_NAME + System.currentTimeMillis();
        user.setUsername(newName);
        assertThat(user.getUsername(), equalTo(newName));
    }

    @Test
    public void newContactShouldBeStored() {
        user.newContact(CONTACT_NAME_1);
        List<Contact> contacts = user.findContactByName(CONTACT_NAME_1);

        assertThat("1 contact should be found", contacts.size() == 1);
        assertThat(CONTACT_NAME_1, equalTo(contacts.get(0).getName()));

        Contact contact1 = getContact1();
        user.addContact(contact1);

        assertThat("Contact 1 should be existed", user.hasContact(contact1));

        contacts = user.findContactByName(CONTACT_NAME_1);
        assertThat("2 contacts should be found", contacts.size() == 2);
        assertThat(contacts, hasItem(contact1));
    }

    @Test
    public void mergeContactsShouldWork() {
        user.addContact(getContact1());
        Contact contact3 = new Contact(CONTACT_NAME_3);
        Contact contact4 = new Contact(CONTACT_NAME_4);
        user.mergeContacts(Arrays.asList(getContact2(), contact3, contact4));

        assertThat(user.getContacts().size(), equalTo(4));
        assertThat(user.getContacts(), hasItem(getContact1()));
        assertThat(user.getContacts(), hasItem(getContact2()));
        assertThat(user.getContacts(), hasItem(contact3));
        assertThat(user.getContacts(), hasItem(contact4));
    }

    @Test
    public void removeContactShouldWork() {
        Contact contact1 = getContact1();
        Contact contact2 = getContact2();

        user.addContact(contact1);
        user.addContact(contact2);

        assertThat("Contact 1 should be there", user.hasContact(contact1));
        assertThat("Contact 2 should be there", user.hasContact(contact2));

        user.removeContact(contact1);

        assertThat("Contact 1 should not be there", !user.hasContact(contact1));
        assertThat("Contact 2 should still be there", user.hasContact(contact2));

        user.newAddressBook(BOOK_NAME_1);
        user.addContactToAddressBook(contact2, BOOK_NAME_1);

        assertThat("Contact 2 should be in address book", user.getAddressBook(BOOK_NAME_1).hasContact(contact2));

        user.removeContact(contact2);

        assertThat("Contact 2 should be removed from user", !user.hasContact(contact2));
        assertThat("Contact 2 should be removed from AddressBook 1", !user.getAddressBook(BOOK_NAME_1).hasContact(contact2));
    }

    @Test
    public void addContactToAddressBookShouldThrowOnNull() {
        Contact contact1 = getContact1();
        assertThrows(NullPointerException.class, () -> user.addContactToAddressBook(null, ""));
        assertThrows(NullPointerException.class, () -> user.addContactToAddressBook(contact1, null));
    }

    @Test
    public void findContactByNameShouldWork() {
        assertThrows(NullPointerException.class, () -> user.findContactByName(null));

        assertThrows(NullPointerException.class, () -> user.findContactByName(null, true));

        user.addContact(getContact1());
        user.addContact(getContact2());

        List<Contact> list = user.findContactByName(CONTACT_NAME_1);
        assertThat(format("Contact '%s' should be found", CONTACT_NAME_1), list.size() > 0);

        list = user.findContactByName(CONTACT_NAME_1.substring(3), true);
        assertThat(format("Partial name '%s' should be found in lenient search", CONTACT_NAME_1.substring(3)),
        list.size() > 0);
    }

    @Test
    public void getAllContactsShouldWork() {
        Contact contact1 = getContact1();
        Contact contact2 = getContact2();

        user.addContact(contact1);
        user.addContact(contact2);
        user.newAddressBook(BOOK_NAME_1);
        user.addContactToAddressBook(contact1, BOOK_NAME_1);
        user.addContactToAddressBook(contact2, BOOK_NAME_1);

        user.addContactToAddressBook(contact2, BOOK_NAME_2);

        assertThat("User should have 2 contact", user.getContacts().size() == 2);
        assertThat("User should have 2 address books", user.getAllAddressBooks().size() == 2);
        assertThat("User should have address book 1", user.getAddressBook(BOOK_NAME_1) != null);
        assertThat("User should have address book 2", user.getAddressBook(BOOK_NAME_2) != null);
        assertThat("Address book 1 should have 2 contacts", user.getAddressBook(BOOK_NAME_1).getContacts().size() == 2);
        assertThat("Address book 2 should have 1 contact", user.getAddressBook(BOOK_NAME_2).getContacts().size() == 1);
    }

    @Test
    public void similarContactShouldBeDetectable() {
        user.addContact(getContact1());
        user.addContact(getContact2());

        Contact dupContact1 = getContact1Duplicate();
        assertThat("Contact should be flagged as similar", user.hasSimilarContact(dupContact1));

        Contact dupContact2 = getContact2Duplicate();
        assertThat("Contact should be flagged as similar", user.hasSimilarContact(dupContact2));

        Contact noDupContact = new Contact(format("%s-%d", CONTACT_NAME_1, System.currentTimeMillis()));
        assertThat("Contact should not be similar", !user.hasSimilarContact(noDupContact));
    }

    @Test
    public void addressBookShouldStore() {
        AddressBook book1 = getAddressBookWith2DifferentContacts();
        User user = new User(USER_NAME);
        user.addAddressBook(book1);

        assertThat(format("Address book '%s' should be existed", BOOK_NAME_1),
            user.getAddressBook(BOOK_NAME_1) != null);

        AddressBook book2 = user.newAddressBook(BOOK_NAME_2);
        Contact contact = getContact1();
        book2.addContact(contact);

        assertThat(format("Address book '%s' should be existed", BOOK_NAME_2),
            user.getAddressBook(BOOK_NAME_2) != null);
        assertThat(format("Address book '%s' should have contact '%s'", BOOK_NAME_2, contact.getName()),
            user.getAddressBook(BOOK_NAME_2).hasContact(contact));
    }

    @Test
    public void duplicateAddressBookShouldNotBeAccepted() {
        AddressBook book1 = getAddressBookWith2DifferentContacts();
        User user = new User(USER_NAME);
        user.addAddressBook(book1);

        AddressBook newBook = user.newAddressBook(BOOK_NAME_1);

        assertThat("New AddressBook should be the same with existed one", book1.equals(newBook));
    }

    @Test
    public void addressBookShouldBeDeletable() {
        AddressBook book1 = getAddressBookWith2DifferentContacts();
        User user = new User(USER_NAME);
        user.addAddressBook(book1);

        assertThat(format("Address book '%s' is existed", BOOK_NAME_1), user.getAddressBook(BOOK_NAME_1) != null);
        user.removeAddressBook(BOOK_NAME_1);
        assertThat(format("Address book '%s' is deleted", BOOK_NAME_1), user.getAddressBook(BOOK_NAME_1) == null);
        assertThat("Non existed AddressBook should be flagged false", !user.removeAddressBook(""));

        user.addAddressBook(book1);
        user.addAddressBook(new AddressBook(BOOK_NAME_2));
        assertThat("User should have 2 address books", user.getAllAddressBooks().size() == 2);
        user.removeAllAddressBook();
        assertThat("User should have 0 address books", user.getAllAddressBooks().size() == 0);
    }

    @Test
    public void commonContactsShouldBePrintable() {
        AddressBook book1 = getAddressBookWith2DifferentContacts();

        AddressBook book2 = getAddressBookWith2DifferentContacts();
        book2.setName(BOOK_NAME_2);
        book2.addContact(new Contact(CONTACT_NAME_3));
        book2.addContact(new Contact(CONTACT_NAME_4));

        User user = new User(USER_NAME);
        user.addAddressBook(book1);
        user.addAddressBook(book2);

        assertThat("Book1 should have 2 contacts", book1.getContacts().size() == 2);
        assertThat("Book2 should have 4 contacts", book2.getContacts().size() == 4);

        List<Contact> commonContacts = user.getCommonContactsInAllBooks();
        assertThat("Common contacts should have 2 entries, found: " + commonContacts.size(), commonContacts.size() == 2);
        assertThat("Contact 1 should be in the common contacts", commonContacts.contains(getContact1()));
        assertThat("Contact 2 should be in the common contacts", commonContacts.contains(getContact2()));
    }
}
