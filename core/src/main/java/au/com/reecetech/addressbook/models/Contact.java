package au.com.reecetech.addressbook.models;

import com.google.i18n.phonenumbers.NumberParseException;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

    private Phone getPhone(String number) {
        return this.phones.stream().filter(phone -> phone.equals(number)).findFirst().orElse(null);
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

    public Phone addPhone(String number) throws NumberParseException {
        Phone newPhone = this.getPhone(number);
        if (newPhone == null) {
            newPhone = new Phone(number);
            this.addPhone(newPhone);
        }
        return newPhone;
    }

    public Phone removePhone(String number) {
        Phone phone = this.getPhone(number);
        this.phones.remove(phone);
        return phone;
    }

    public boolean hasPhone(Phone phone) {
        return this.phones.contains(phone);
    }

    public boolean hasPhone(String number) {
        return this.hasPhone(this.getPhone(number));
    }

    public boolean isPotentialDuplicate(Contact other) {
        // we need to check whether this.name equals to other.name
        // or, one of the phone number in this.phones equals to one of the phone in other.phones
        // this is O(n^2), but in reality, a contact should have a small number of phones
        return this.name.equalsIgnoreCase(other.getName()) ||
            this.phones.stream()
                .filter(phone ->
                    other.getPhones().stream().filter(phone::equals).count() > 0)
                .count() > 0;
    }
}
