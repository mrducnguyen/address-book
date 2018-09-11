package au.com.reecetech.addressbook.store;

import au.com.reecetech.addressbook.model.AddressBook;
import au.com.reecetech.addressbook.model.User;

import java.util.Collection;

public interface IUserStorage {

    User getOrCreateUser(String username);
    Collection<AddressBook> getAllAddressBooks(String username);
    AddressBook getOrCreateAddressBook(String username, String name);

}
