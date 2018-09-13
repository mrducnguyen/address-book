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

    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }

    public boolean hasContact(Contact contact) {
        return this.contacts.contains(contact);
    }

    public boolean hasSimilarContact(Contact contact) {
        return this.contacts.stream().filter(contact::isPotentialDuplicate).count() > 0;
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
    }

    public List<Contact> findContactByName(String name) {
        return this.findContactByName(name, false);
    }

    public List<Contact> findContactByName(String name, boolean lenient) {
        return this.contacts.stream()
            .filter(contact -> lenient
                ? contact.getName().toUpperCase().contains(name.toUpperCase())
                : contact.getName().equals(name)
            )
            .collect(Collectors.toList());
    }

    public List<Contact> intersectionContacts(AddressBook other) {
        return this.contacts.stream()
            .filter(other.getContacts()::contains)
            .collect(Collectors.toList());
    }

    public void mergeContacts(AddressBook other) {
        this.contacts = Stream.concat(this.contacts.stream(), other.getContacts().stream())
            .collect(Collectors.toList());
    }
}
