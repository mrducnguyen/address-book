package au.com.reecetech.addressbook.store;

import au.com.reecetech.addressbook.models.AddressBook;
import au.com.reecetech.addressbook.models.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryStorage {
    private Map<String, User> users;

    public MemoryStorage() {
        this.users = new HashMap<>();
    }

    public User addUser(String username) {
        User user = new User(username);
        this.users.put(username, user);
        return user;
    }
}
