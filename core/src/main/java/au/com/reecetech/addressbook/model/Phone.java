package au.com.reecetech.addressbook.model;

import au.com.reecetech.addressbook.enums.PhoneType;

public class Phone {
    private String number;
    private PhoneType type;

    public Phone() {}

    public Phone(String number, PhoneType type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public PhoneType getType() {
        return type;
    }

    public void setType(PhoneType type) {
        this.type = type;
    }
}
