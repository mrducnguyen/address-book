package au.com.reecetech.addressbook.models;

import java.util.*;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

public class User {

    @NotNull
    @NotEmpty
    @MinLength(value = 5)
    private String username;
    private Map<String, AddressBook> addressBooks;
    private List<Contact> contacts;

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

    public AddressBook getAddressBook(String name) {
        return this.addressBooks.get(name);
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
    public void deleteAdressBook(String name) {
        if (this.addressBooks.containsKey(name)) {
            this.addressBooks.remove(name);
        }
    }

    public List<Contact> getCommonContactsInAllBooks() {
        // MapReduce, natural parallel operations
        // so parallelStream is used
        return this.addressBooks.values().parallelStream()
            .map(AddressBook::getContacts)
            .reduce(new ArrayList<>(), (list1, list2) -> {
                List<Contact> newList = new ArrayList<>(list1);
                newList.retainAll(list2);
                return newList;
            });
    }
}
