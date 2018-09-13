package au.com.reecetech.addressbook.services;

import au.com.reecetech.addressbook.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactService implements IContactService {

    @Override
    public List<Contact> potentialDuplicates(List<Contact> contacts) {
        // we need to check each contact against all other contacts
        // this is O(n^2)
        // the idea is:
        //  - for each contact check it against all other contacts, which mean
        //     -- excluding itself
        //     -- excluding all found duplicates
        List<Contact> duplicates = new ArrayList<>();
        contacts.stream()
        .forEach(contact -> {
            int startSize = duplicates.size(); // store the current duplicates size
            contacts.stream()
            .filter(otherContact -> !contact.equals(otherContact) && !duplicates.contains(otherContact))
            .filter(contact::isPotentialDuplicate)
            .map(duplicateContact -> duplicates.add(duplicateContact))
            .count(); // map is an intermediate operation and won't execute without a terminal statement
            // we know that there is duplicates when the size of the list increase
            // after the previous step
            if (duplicates.size() > startSize) {
                // add the current contact to the duplicate list
                // along with all the other duplicates
                duplicates.add(contact);
            }
        });
        return duplicates;
    }
}
