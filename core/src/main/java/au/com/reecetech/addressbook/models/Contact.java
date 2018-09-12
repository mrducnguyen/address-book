package au.com.reecetech.addressbook.models;

import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Contact {

    @NotNull
    @NotEmpty
    @MinLength(value = 5)
    private String name;
    private List<Phone> phones;

    public Contact(String name) {
        this.name = name;
        this.phones = new ArrayList<>();
    }

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

    public void addPhone(Phone phone) {
        this.phones.add(phone);
    }

    public boolean hasPhone(Phone phone) {
        return this.phones.contains(phone);
    }
}
