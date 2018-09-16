package au.com.reecetech.addressbook.models;

import java.util.*;

import au.com.reecetech.addressbook.util.ContactUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Delegate;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotEmpty;

public class User {

    private static final String DEFAULT_BOOK_NAME = "__DEFAULT_BOOK__";

    @NotEmpty
    @MinLength(value = 5)
    @NonNull
    @Getter @Setter private String username;

    // we maintain a default address book to store all user's contacts
    // we use Lombok to generate all public methos of default address book to this user object
    // Decorator pattern
    private interface DelegateExcludes {
        void setName(String name);
        void setContacts(List<Contact> contacts);
        void removeContact(Contact contact);
        List<Contact> intersectionContacts(AddressBook other);
        void mergeContacts(AddressBook other);
    }

    @Delegate(excludes = DelegateExcludes.class)
    private AddressBook defaultBook;

    private Map<String, AddressBook> addressBooks;

    public User(@NonNull String username) {
        this.username = username;
        this.defaultBook = new AddressBook(DEFAULT_BOOK_NAME);
        this.addressBooks = new HashMap<>();
    }

    public AddressBook getAddressBook(String name) {
        return this.addressBooks.get(name);
    }

    public Collection<AddressBook> getAllAddressBooks() {
        return this.addressBooks.values();
    }

    public void addAddressBook(AddressBook book) {
        this.addressBooks.put(book.getName(), book);
    }

    public Contact newContact(String name) {
        Contact newContact = new Contact(name);
        this.defaultBook.addContact(newContact);
        return newContact;
    }

    public void removeContact(Contact contact) {
        this.defaultBook.removeContact(contact);
        this.addressBooks.values().stream()
            .forEach(addressBook -> addressBook.removeContact(contact));
    }

    /**
     * Add contact to address book. An {@link AddressBook} will be created if it is not existed yet
     * @param contact          the contact to be added
     * @param addressBookName  the address book's name
     */
    public void addContactToAddressBook(@NonNull Contact contact, @NonNull String addressBookName) {
        AddressBook book = this.getAddressBook(addressBookName);
        if (book == null) {
            book = this.newAddressBook(addressBookName);
        }
        book.addContact(contact);
    }

    /**
     * Create an {@link AddressBook} with supplied <pre>name</pre>. If the address book is already existed, a reference to the
     * existed address book will be returned
     * @param name  - The address book's name, should be unique
     * @return a new {@link AddressBook}, or the existing instance
     */
    public final AddressBook newAddressBook(String name) {
        if (!this.addressBooks.containsKey(name)) {
            this.addressBooks.put(name, new AddressBook(name));
        }
        return this.addressBooks.get(name);
    }

    /**
     * Delete the {@link AddressBook} with the supplied <pre>name</pre>
     * @param name - The name of the address book to delete
     */
    public boolean removeAddressBook(String name) {
        if (this.addressBooks.containsKey(name)) {
            this.addressBooks.remove(name);
            return true;
        }
        return false;
    }

    public void removeAllAddressBook() {
        this.addressBooks.clear();
    }

    public List<Contact> getCommonContactsInAllBooks() {
        return ContactUtil.commonContacts(this.addressBooks.values());
    }
}
