package au.com.reecetech.addressbook.models;

import java.util.*;

import au.com.reecetech.addressbook.util.ContactUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotEmpty;

public class User {

    @NotEmpty
    @MinLength(value = 5)
    @NonNull
    @Getter @Setter private String username;

    @Getter private List<Contact> contacts;

    private Map<String, AddressBook> addressBooks;

    public User(@NonNull String username) {
        this.username = username;
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
        return ContactUtil.commonContacts(this.addressBooks.values().toArray(new AddressBook[this.addressBooks.size()]));
    }
}
