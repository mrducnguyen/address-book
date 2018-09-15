package au.com.reecetech.addressbook;

import au.com.reecetech.addressbook.models.AddressBook;
import au.com.reecetech.addressbook.models.Contact;
import au.com.reecetech.addressbook.models.Phone;
import au.com.reecetech.addressbook.models.User;
import au.com.reecetech.addressbook.store.IUserStorage;
import au.com.reecetech.addressbook.store.MemoryStorage;
import com.budhash.cliche.Command;
import com.budhash.cliche.Shell;
import com.google.i18n.phonenumbers.NumberParseException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class AddressBookConsoleApp {

    private IUserStorage store;
    private User user;

    private Shell shell;

    public AddressBookConsoleApp() {
        this.store = new MemoryStorage();
    }

    public void cliSetShell(Shell shell) {
        this.shell = shell;
    }

    @Command(description = "Create new user, usage: user [username]")
    public String user(String username) {
        this.user = this.store.getOrCreateUser(username);
        return format("User '%s' was created and selected", username);
    }

    @Command(description = "Create new address book, usage: ab [addressBookName]")
    public String addressBook(String bookName) {
        if (this.user == null) {
            return "Please create a user first";
        }
        this.user.newAddressBook(bookName);
        return format("Address book '%s' was created", bookName);
    }

    @Command(description = "Create new contact, usage: contact [contactName] [contactPhone...]")
    public String contact(String... args) throws NumberParseException {
        if (this.user == null) {
            return "Please create a user first";
        }
        Contact contact = this.user.newContact(args[0]);
        for (int i = 1; i < args.length; i++) {
            contact.addPhone(args[i]);
        }
        return format("Contact '%s' was created", contact.getName());
    }

    @Command(description = "Delete a contact, usage: delete [contactName]")
    public String delete(String name) {
        if (this.user == null) {
            return "Please create a user first";
        }
        List<Contact> list = this.user.findContactByName(name);

        if (list.size() == 0) {
            return format("Found 0 contact with name '%s'", name);
        }
        Contact contact = list.get(0);
        this.user.removeContact(contact);
        return format("Contact '%s' has been deleted", name);
    }

    @Command(description = "Add a contact to an address book, new address book will be created if it doesn't exist\n" +
        ", usage: add [contactName] [addressBookName]")
    public String add(String name, String bookName) {
        if (this.user == null) {
            return "Please create a user first";
        }

        List<Contact> list = this.user.findContactByName(name);

        if (list.size() == 0) {
            return format("Found 0 contact with name '%s'", name);
        }
        Contact contact = list.get(0);
        this.user.addContactToAddressBook(contact, bookName);
        return format("Contact '%s' has been added to address book '%s'", name, bookName);
    }

    @Command(description = "List all contacts")
    public String listContact() {
        if (this.user == null) {
            return "Please create a user first";
        }

        List<Contact> contacts = this.user.getAllContacts();
        Iterator<Contact> iter = contacts.iterator();
        int count = 0;
        while (iter.hasNext()) {
            Contact contact = iter.next();
            System.out.println(format("%d. %s - %s", ++count, contact.getName(),
                contact.getPhones().stream().map(Phone::toString).collect(Collectors.joining(", "))));
        }
        return "";
    }

    @Command(description = "List all address books")
    public String listBook() {
        if (this.user == null) {
            return "Please create a user first";
        }

        Collection<AddressBook> addressBooks = this.user.getAllAddressBooks();
        Iterator<AddressBook> iter = addressBooks.iterator();
        int count = 0;
        while (iter.hasNext()) {
            System.out.println(format("%d. %s", ++count, iter.next().getName()));
        }
        return "";
    }

    @Command(description = "List all contacts in address books")
    public String listContactInBook(String bookname) {
        if (this.user == null) {
            return "Please create a user first";
        }

        AddressBook book = this.user.getAddressBook(bookname);
        List<Contact> contacts = book.getContacts();
        Iterator<Contact> iter = contacts.iterator();
        int count = 0;
        while (iter.hasNext()) {
            System.out.println(format("%d. %s", ++count, iter.next().getName()));
        }
        return "";
    }

    @Command(description = "List common contacts in all address books")
    public String listCommonContact() {
        if (this.user == null) {
            return "Please create a user first";
        }

        List<Contact> contacts = this.user.getCommonContactsInAllBooks();
        Iterator<Contact> iter = contacts.iterator();
        int count = 0;
        while (iter.hasNext()) {
            System.out.println(format("%d. %s", ++count, iter.next().getName()));
        }
        return "";
    }
}
