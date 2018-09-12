package au.com.reecetech.addressbook.models;

import com.google.i18n.phonenumbers.NumberParseException;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ContactTest {

    private static final String CONTACT_NAME = "Mr. Chewie";
    private static final String CONTACT_NAME_2 = "Mr. Chewbacca";
    private static final String PHONE_NUMBER = "0388885555";
    private static final String PHONE_NUMBER_2 = "0366665555";
    private static final String PHONE_NUMBER_3 = "0366667777";
    private static final String PHONE_NUMBER_4 = "0366669999";

    @Test
    public void emptyContactNameShouldBeInvalid() {
        Validator validator = new Validator();

        Contact empty1 = new Contact(null);
        List<ConstraintViolation> errorList = validator.validate(empty1);

        assertThat(errorList.size(), greaterThan(0));
        ConstraintViolation violation = errorList.get(0);
        assertThat(violation.getMessage(), containsString("cannot be null"));

        Contact empty2 = new Contact("");
        errorList = validator.validate(empty2);

        assertThat(errorList.size(), greaterThan(0));
        violation = errorList.get(0);
        assertThat(violation.getMessage(), containsString("cannot be empty"));
    }

    @Test
    public void contactPhoneShouldBeSetByString() throws NumberParseException {
        Contact contact = new Contact(CONTACT_NAME);
        contact.addPhone(PHONE_NUMBER);
        Phone phone = new Phone(PHONE_NUMBER);

        assertThat("Phone number should be added", contact.hasPhone(PHONE_NUMBER));
        assertThat("Phone number should be existed", contact.hasPhone(phone));
    }

    @Test
    public void contactPhoneShouldBeSetByObject() throws NumberParseException {
        Contact contact = new Contact(CONTACT_NAME);
        Phone phone = new Phone(PHONE_NUMBER);
        contact.addPhone(phone);

        assertThat("Phone number should be added", contact.hasPhone(phone));
        assertThat("Phone number should be existed", contact.hasPhone(PHONE_NUMBER));
    }

    @Test
    public void duplicateNameContactShouldBeDetected() {
        Contact contact1 = new Contact(CONTACT_NAME);
        Contact contact2 = new Contact(CONTACT_NAME);

        assertThat("Contact 1 should be potentially duplicated with Contact 2", contact1.isPotentialDuplicate(contact2));
    }

    @Test
    public void duplicatePhoneContactShouldBeDetected() throws NumberParseException {
        Contact contact1 = new Contact(CONTACT_NAME);
        Contact contact2 = new Contact(CONTACT_NAME_2);

        contact1.addPhone(PHONE_NUMBER);
        contact1.addPhone(PHONE_NUMBER_2);

        contact2.addPhone(PHONE_NUMBER);
        contact2.addPhone(PHONE_NUMBER_3);
        contact2.addPhone(PHONE_NUMBER_4);

        assertThat("Contact 1 should be potentially duplicated with Contact 2", contact1.isPotentialDuplicate(contact2));
    }
}
