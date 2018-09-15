package au.com.reecetech.addressbook.util;

import au.com.reecetech.addressbook.models.AddressBook;
import au.com.reecetech.addressbook.models.Contact;

import java.util.ArrayList;
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
        contacts.stream()
        .forEach(contact -> {
            long dupCount = contacts.stream()
                // filter itself out
                .filter(otherContact -> !contact.equals(otherContact) && !duplicates.contains(otherContact))
                // filter all the ones which are in duplicates already
                .filter(contact::isPotentialDuplicate)
                .map(duplicateContact -> duplicates.add(duplicateContact))
                .count();

            if (dupCount > 0) {
                // add the current contact to the duplicate list
                // along with all the other duplicates
                duplicates.add(contact);
            }
        });
        return duplicates;
    }

    public static List<Contact> commonContacts(AddressBook... books) {

        assert books.length > 0 : "Expecting at least 1 AddressBook";

        if (books.length == 1) {
            return books[0].getContacts();
        }

        return Stream.of(books).parallel()
            .map(AddressBook::getContacts)
            .reduce((list1, list2) -> {
                List<Contact> newList = new ArrayList<>(list1);
                newList.retainAll(list2);
                return newList;
            })
            .get();
    }
}
