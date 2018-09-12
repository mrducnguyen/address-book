package au.com.reecetech.addressbook.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import au.com.reecetech.addressbook.models.AddressBook;

public class User {
    private String username;
    private Map<String, AddressBook> addressBooks;

    public User() {}

    public User(String username) {
        this.username = username;
        this.addressBooks = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public final Collection<AddressBook> getAllAddressBooks() {
        return this.addressBooks.values();
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
    public void deleteAdressBook(String name) {
        if (this.addressBooks.containsKey(name)) {
            this.addressBooks.remove(name);
        }
    }
}
