package au.com.reecetech.addressbook;

import au.com.reecetech.addressbook.models.AddressBook;
import au.com.reecetech.addressbook.models.Contact;
import au.com.reecetech.addressbook.models.Phone;
import au.com.reecetech.addressbook.models.User;
import au.com.reecetech.addressbook.store.IUserStorage;
import au.com.reecetech.addressbook.store.MemoryStorage;
import au.com.reecetech.addressbook.util.ContactUtil;
import com.budhash.cliche.Command;
import com.budhash.cliche.Shell;
import com.google.i18n.phonenumbers.NumberParseException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
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

    @Command(description = "Create new address book, usage: book [addressBookName]")
    public String book(String bookName) {
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

        List<Contact> list = this.user.findContactByName(name, true);

        if (list.size() == 0) {
            return format("Found 0 contact with name '%s'", name);
        } else if (list.size() == 1) {
            Contact contact = list.get(0);
            this.user.addContactToAddressBook(contact, bookName);
            return format("Contact '%s' has been added to address book '%s'", name, bookName);
        }

        return chooseContact(list, bookName);
    }

    @Command(description = "List entities in the memory. Usage: list contact, list book, list contact [bookName], list contact unique")
    public String list(String... args) {
        String entity = args[0];
        switch (entity.toLowerCase()) {
            case "book":
            case "addressbook":
                return listBook();
            case "contact":
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("unique")) {
                        return listCommonContacts();
                    } else if (args[1].equalsIgnoreCase("duplicate")) {
                        return listDuplicateContacts();
                    }
                    return listContactInBook(args[1]);
                }
                return listContact();
        }
        return "Usage: \n" +
        "\tlist contact               List all contacts\n" +
        "\tlist book|addressbook      List all address books\n" +
        "\n" +
        "\tlist contact [bookName]    List all contacts in address book [bookName]\n" +
        "\tlist contact unique        List all unique contacts in all address books\n" +
        "\tlist contact duplicate     List all potential duplicate contacts\n";
    }

    private String printContact(Contact contact) {
        return format("%s (%s)", contact.getName(),
        contact.getPhones().stream().map(Phone::toString).collect(Collectors.joining(", ")));
    }

    private String chooseContact(List<Contact> list, String bookName) {
        int i = 0;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println("Please choose a contact (type the number): ");
            for (i = 0; i < list.size(); i++) {
                System.out.println(format("%d. %s", i + 1, printContact(list.get(i))));
            }
            System.out.println(format("%d. All", i + 1));
            System.out.println(format("%d. Cancel", i + 2));
            System.out.print("Your choice: ");
            try {
                int choice = scan.nextInt();
                if (choice > 0 && choice <= list.size()) {
                    Contact contact = list.get(choice - 1);
                    this.user.addContactToAddressBook(contact, bookName);
                    return format("Contact '%s' has been added to address book '%s'", printContact(contact), bookName);
                } else if (choice == list.size() + 1) {
                    for (i = 0; i < list.size(); i++) {
                        this.user.addContactToAddressBook(list.get(i), bookName);
                    }
                    return format("All matched contacts has been added to '%s'", bookName);
                } else if (choice == list.size() + 2) {
                    return "Cancel";
                }
            } catch (Exception e) {
                System.out.println("!!! Wrong choice !!!");
                String s = scan.next(); // clear input
            }
        } while (true);
    }

    private String listContact() {
        if (this.user == null) {
            return "Please create a user first";
        }

        List<Contact> contacts = this.user.getContacts();
        Iterator<Contact> iter = contacts.iterator();
        int count = 0;
        while (iter.hasNext()) {
            Contact contact = iter.next();
            System.out.println(format("%d. %s", ++count, printContact(contact)));
        }
        return "";
    }

    private String listBook() {
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

    private String listContactInBook(String bookname) {
        if (this.user == null) {
            return "Please create a user first";
        }

        AddressBook book = this.user.getAddressBook(bookname);
        if (book == null) {
            return format("Cannot find address book name '%s'", bookname);
        }
        List<Contact> contacts = book.getContacts();
        Iterator<Contact> iter = contacts.iterator();
        int count = 0;
        while (iter.hasNext()) {
            System.out.println(format("%d. %s", ++count, printContact(iter.next())));
        }
        return "";
    }

    private String listCommonContacts() {
        if (this.user == null) {
            return "Please create a user first";
        }

        List<Contact> contacts = this.user.getCommonContactsInAllBooks();
        Iterator<Contact> iter = contacts.iterator();
        int count = 0;
        while (iter.hasNext()) {
            System.out.println(format("%d. %s", ++count, printContact(iter.next())));
        }
        return "";
    }

    private String listDuplicateContacts() {
        if (this.user == null) {
            return "Please create a user first";
        }
        List<Contact> contacts = ContactUtil.potentialDuplicates(user.getContacts());
        Iterator<Contact> iter = contacts.iterator();
        int count = 0;
        while (iter.hasNext()) {
            System.out.println(format("%d. %s", ++count, printContact(iter.next())));
        }
        return "";
    }
}
