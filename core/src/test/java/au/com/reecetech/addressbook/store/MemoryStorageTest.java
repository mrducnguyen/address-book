package au.com.reecetech.addressbook.store;

import au.com.reecetech.addressbook.AbstractTest;
import au.com.reecetech.addressbook.models.AddressBook;
import au.com.reecetech.addressbook.models.Contact;
import au.com.reecetech.addressbook.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemoryStorageTest extends AbstractTest {

    private IUserStorage userStorage;

    @BeforeEach
    public void createTheStore() {
        this.userStorage = new MemoryStorage();
    }

    @Test
    public void newUserShouldBeCreated() {
        User user = this.userStorage.getOrCreateUser(USER_NAME);
        assertThat("Username existed", this.userStorage.getOrCreateUser(USER_NAME).equals(user));
    }

    @Test
    public void nullParametersShouldBeFlagged() {
        assertThrows(NullPointerException.class, () -> userStorage.getOrCreateUser(null));
        assertThrows(NullPointerException.class, () -> userStorage.getAllAddressBooks(null));
    }

    @Test
    public void newAddressBookShouldBeCreatedForUser() {
        AddressBook book1 = getAddressBookWith2DifferentContacts();
        AddressBook book2 = new AddressBook(BOOK_NAME_2);
        book2.addContact(new Contact(CONTACT_NAME_3));
        book2.addContact(new Contact(CONTACT_NAME_4));

        User user = this.userStorage.getOrCreateUser(USER_NAME);
        user.addAddressBook(book1);
        user.addAddressBook(book2);

        assertThat("Non existed user should have have any address book", userStorage.getAllAddressBooks("RANDOM") == null);

        Collection<AddressBook> allBooks = userStorage.getAllAddressBooks(USER_NAME);
        assertThat("There are 2 books", allBooks.size() == 2);
        assertThat("Book 1 is existed", allBooks.contains(book1));
        assertThat("Book 2 is existed", allBooks.contains(book2));
    }
}
