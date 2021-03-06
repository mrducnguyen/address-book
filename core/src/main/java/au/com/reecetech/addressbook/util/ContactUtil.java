package au.com.reecetech.addressbook.util;

import au.com.reecetech.addressbook.models.AddressBook;
import au.com.reecetech.addressbook.models.Contact;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class ContactUtil {

    public static List<Contact> potentialDuplicates(List<Contact> contacts) {
        // we need to check each contact against all other contacts
        // this is O(n^2)
        // the idea is:
        //  - for each contact check it against all other contacts, which mean
        //     -- excluding itself
        //     -- excluding all found duplicates
        List<Contact> duplicates = new ArrayList<>();

        for (int i = 0; i < contacts.size() - 1; i++) {
            int count = 0;
            for (int j = i + 1; j < contacts.size(); j++) {
                if (contacts.get(i).isPotentialDuplicate(contacts.get(j)) && !duplicates.contains(contacts.get(j))) {
                    duplicates.add(contacts.get(j));
                    ++count;
                }
            }
            if (count > 0 && !duplicates.contains(contacts.get(i))) {
                duplicates.add(contacts.get(i));
            }
        }

        return duplicates;
    }

    public static List<Contact> intersection(List<Contact> list1, List<Contact> list2) {
        List<Contact> newList = new ArrayList<>(list1);
        newList.retainAll(list2);
        return newList;
    }

    public static List<Contact> commonContacts(@NonNull Collection<AddressBook> books) {

        assert books.size() > 0 : "Expecting at least 1 AddressBook";

        if (books.size() == 1) {
            return books.stream().findFirst().get().getContacts();
        }

        return books.parallelStream()
            .map(AddressBook::getContacts)
            .reduce(ContactUtil::intersection)
            .get();
    }
}
