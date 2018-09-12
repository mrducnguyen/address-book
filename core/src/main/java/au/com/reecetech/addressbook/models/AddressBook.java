package au.com.reecetech.addressbook.models;

import java.util.List;

public class AddressBook {
    private String name;
    private List<Contact> contacts;

    public AddressBook() {}

    public AddressBook(String name) {
        this.name = name;
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


}
