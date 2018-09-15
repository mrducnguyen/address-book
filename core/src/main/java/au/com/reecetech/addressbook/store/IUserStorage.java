package au.com.reecetech.addressbook.store;

import au.com.reecetech.addressbook.models.AddressBook;
import au.com.reecetech.addressbook.models.User;

import java.util.Collection;

public interface IUserStorage {

    User getOrCreateUser(String username);
    Collection<AddressBook> getAllAddressBooks(String username);
}
