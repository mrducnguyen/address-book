package au.com.reecetech.addressbook.models;

import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserTest extends AbstractModelTest {

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
        user.deleteAdressBook(BOOK_NAME_1);
        assertThat(format("Address book '%s' is deleted", BOOK_NAME_1), user.getAddressBook(BOOK_NAME_1) == null);
    }

    @Test
    public void commonContactsShouldBePrintable() {
        // TODO: implement test
    }
}
