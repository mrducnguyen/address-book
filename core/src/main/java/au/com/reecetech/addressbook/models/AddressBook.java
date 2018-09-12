package au.com.reecetech.addressbook.models;

import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddressBook {

    @NotNull
    @NotEmpty
    @MinLength(value = 5)
    private String name;
    private List<Contact> contacts;

    public AddressBook(String name) {
        this.name = name;
        this.contacts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
    }

    public List<Contact> findContactByName(String name) {
        return this.contacts.stream()
            .filter(contact -> contact.getName().equals(name))
            .collect(Collectors.toList());
    }

    public List<Contact> intersectionContacts(AddressBook other) {
        return this.contacts.stream()
            .filter(other.getContacts()::contains)
            .collect(Collectors.toList());
    }

    public List<Contact> potentialDuplicates() {
        // we need to check each contact against all other contacts
        // this is O(n^2)
        // the idea is:
        //  - for each contact check it against all other contacts, which mean
        //     -- excluding itself
        //     -- excluding all found duplicates
        List<Contact> duplicates = new ArrayList<>();
        this.contacts.stream()
            .forEach(contact -> {
                int startSize = duplicates.size(); // store the current duplicates size
                this.contacts.stream()
                    .filter(otherContact -> !contact.equals(otherContact) && !duplicates.contains(otherContact))
                    .filter(contact::isPotentialDuplicate)
                    .map(duplicateContact -> duplicates.add(duplicateContact));
                // we know that there is duplicates when the size of the list increase
                // after the previous step
                if (duplicates.size() > startSize) {
                    // add the current contact to the duplicate list
                    // along with all the other duplicates
                    duplicates.add(contact);
                }
            });
        return duplicates;
    }

    public void mergeContacts(AddressBook other) {
        this.contacts = Stream.concat(this.contacts.stream(), other.getContacts().stream())
            .collect(Collectors.toList());
    }
}
