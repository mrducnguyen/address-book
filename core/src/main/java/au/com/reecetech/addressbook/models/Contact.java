package au.com.reecetech.addressbook.models;

import com.google.i18n.phonenumbers.NumberParseException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class Contact {

    @NotEmpty
    @MinLength(value = 5)
    @NonNull
    @Getter @Setter private String name;

    @Getter @Setter private List<Phone> phones;

    public Contact(@NonNull String name) {
        this.name = name;
        this.phones = new ArrayList<>();
    }

    private Phone getPhone(String number) {
        return this.phones.stream().filter(phone -> phone.equals(number)).findFirst().orElse(null);
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
                .anyMatch(phone -> other.getPhones().stream().anyMatch(phone::equals));
    }

    @Override
    public boolean equals(Object obj) {
        // Contact should be considered equals when. Is this a good design?!!
        //   - name is equals
        //   - AND phones are all equals
        if (obj instanceof Contact) {
            Contact other = (Contact)obj;
            if (this.name.equals(other.getName())) {
                return other.getPhones().stream().allMatch(this.phones::contains);
            }
        }

        return false;
    }
}
