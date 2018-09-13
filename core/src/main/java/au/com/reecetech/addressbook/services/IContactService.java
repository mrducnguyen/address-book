package au.com.reecetech.addressbook.services;

import au.com.reecetech.addressbook.models.Contact;

import java.util.List;

public interface IContactService {
    List<Contact> potentialDuplicates(List<Contact> contacts);
}
