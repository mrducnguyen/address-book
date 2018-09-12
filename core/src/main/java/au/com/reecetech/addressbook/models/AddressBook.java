package au.com.reecetech.addressbook.models;

import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.HashSet;
import java.util.Set;

public class AddressBook {

    @NotNull
    @NotEmpty
    @MinLength(value = 5)
    private String name;
    private Set<Contact> contacts;

    public AddressBook(String name) {
        this.name = name;
        this.contacts = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }


}
