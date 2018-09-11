package au.com.reecetech.addressbook.model;

import java.util.List;

public class Contact {
    private long id;
    private String name;
    private List<Phone> phones;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
}
