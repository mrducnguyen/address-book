package au.com.reecetech.addressbook.models;

import au.com.reecetech.addressbook.util.ContactUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddressBook {

    @NotEmpty
    @MinLength(value = 5)
    @NonNull
    @Getter @Setter private String name;

    @Getter @Setter private List<Contact> contacts;

    public AddressBook(@NonNull String name) {
        this.name = name;
        this.contacts = new ArrayList<>();
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

    public List<Contact> findContactByName(@NonNull String name) {
        return this.findContactByName(name, false);
    }

    public List<Contact> findContactByName(@NonNull String name, boolean lenient) {
        return this.contacts.stream()
            .filter(contact -> lenient
                ? contact.getName().toUpperCase().contains(name.toUpperCase())
                : contact.getName().equals(name)
            )
            .collect(Collectors.toList());
    }

    public List<Contact> intersectionContacts(AddressBook other) {
        return ContactUtil.intersection(this.contacts, other.getContacts());
    }

    public void mergeContacts(AddressBook other) {
        this.contacts = Stream.concat(this.contacts.stream(), other.getContacts().stream())
            .collect(Collectors.toList());
    }
}
