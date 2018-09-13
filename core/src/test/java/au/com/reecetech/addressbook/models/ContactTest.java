package au.com.reecetech.addressbook.models;

import com.google.i18n.phonenumbers.NumberParseException;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ContactTest extends AbstractModelTest {

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
        Contact contact = new Contact(CONTACT_NAME_1);
        contact.addPhone(PHONE_NUMBER_1);
        Phone phone = new Phone(PHONE_NUMBER_1);

        assertThat("Phone number should be added", contact.hasPhone(PHONE_NUMBER_1));
        assertThat("Phone number should be existed", contact.hasPhone(phone));
    }

    @Test
    public void contactPhoneShouldBeSetByObject() throws NumberParseException {
        Contact contact = new Contact(CONTACT_NAME_1);
        Phone phone = new Phone(PHONE_NUMBER_1);
        contact.addPhone(phone);

        assertThat("Phone number should be added", contact.hasPhone(phone));
        assertThat("Phone number should be existed", contact.hasPhone(PHONE_NUMBER_1));
    }

    @Test
    public void contactPhoneShouldBeRemovedByString() throws NumberParseException {
        Contact contact = new Contact(CONTACT_NAME_1);
        contact.addPhone(PHONE_NUMBER_1);
        contact.addPhone(PHONE_NUMBER_2);
        contact.addPhone(PHONE_NUMBER_3);
        contact.addPhone(PHONE_NUMBER_4);

        contact.removePhone(PHONE_NUMBER_1);
        assertThat(format("Phone '%s' should be removed", PHONE_NUMBER_1), !contact.hasPhone(PHONE_NUMBER_1));
        assertThat(format("Phone '%s' should still be there", PHONE_NUMBER_2), contact.hasPhone(PHONE_NUMBER_2));
    }

    @Test
    public void contactPhoneShouldNotBeAddedTwice() throws NumberParseException {
        Contact contact = new Contact(CONTACT_NAME_1);
        contact.addPhone(PHONE_NUMBER_1);
        contact.addPhone(PHONE_NUMBER_1);

        assertThat("Only 1 phone should be added", contact.getPhones().size() == 1);

        contact.addPhone(PHONE_NUMBER_2);
        contact.addPhone(PHONE_NUMBER_2);

        assertThat("Only 2 phone should be added", contact.getPhones().size() == 2);
    }

    @Test
    public void duplicateNameContactShouldBeDetected() {
        Contact contact1 = new Contact(CONTACT_NAME_1);
        Contact contact2 = new Contact(CONTACT_NAME_1);

        assertThat("Contact 1 should be potentially duplicated with Contact 2", contact1.isPotentialDuplicate(contact2));
    }

    @Test
    public void duplicatePhoneContactShouldBeDetected() throws NumberParseException {
        Contact contact1 = new Contact(CONTACT_NAME_1);
        Contact contact2 = new Contact(CONTACT_NAME_2);
        Contact contact3 = new Contact(CONTACT_NAME_2);

        contact1.addPhone(PHONE_NUMBER_1);
        contact1.addPhone(PHONE_NUMBER_2);

        contact2.addPhone(PHONE_NUMBER_3);
        contact2.addPhone(PHONE_NUMBER_4);
        contact2.addPhone(PHONE_NUMBER_1);

        contact3.addPhone(PHONE_NUMBER_4);

        assertThat("Contact 1 should be potentially duplicated with Contact 2", contact1.isPotentialDuplicate(contact2));
        assertThat("Contact 1 should not be a duplicate of contact 3", !contact1.isPotentialDuplicate(contact3));
    }
}
