package au.com.reecetech.addressbook;

import com.budhash.cliche.ShellFactory;

import java.io.IOException;

public class Main {

    public  static void main(String[] args) throws IOException {
        System.out.println("Type ?, ?list for all commands");
        System.out.println("?help [command] for command details");
        System.out.println("exit, to quit");
        ShellFactory.createConsoleShell("command", "address-book", new AddressBookConsoleApp())
            .commandLoop();
    }
}
