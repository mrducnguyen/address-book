package au.com.reecetech.addressbook.store;

import au.com.reecetech.addressbook.models.AddressBook;
import au.com.reecetech.addressbook.models.User;
import lombok.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryStorage implements IUserStorage {
    private Map<String, User> users;

    public MemoryStorage() {
        this.users = new HashMap<>();
    }

    @Override
    public User getOrCreateUser(@NonNull String username) {
        if (!this.users.containsKey(username)) {
            this.users.put(username, new User(username));
        }
        return this.users.get(username);
    }

    @Override
    public Collection<AddressBook> getAllAddressBooks(@NonNull String username) {
        User user = this.users.get(username);
        if (user != null) {
            return user.getAllAddressBooks();
        }
        return null;
    }
}
