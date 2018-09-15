package au.com.reecetech.addressbook.models;

import au.com.reecetech.addressbook.AbstractTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest extends AbstractTest {

    @Test
    public void nullNameShouldBeThrown() {
        assertThrows(NullPointerException.class, () -> new User(null));

        assertThrows(NullPointerException.class, () -> new User(USER_NAME).setUsername(null));
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
