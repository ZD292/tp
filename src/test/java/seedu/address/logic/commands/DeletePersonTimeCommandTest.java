package seedu.address.logic.commands;

import javafx.collections.ObservableList;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.*;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupRemark;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.testutil.Assert.assertThrows;

public class DeletePersonTimeCommandTest {

    Person validPerson = new PersonBuilder().build();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeletePersonTimeCommand(null, null));
    }

    @Test
    public void execute_personTimeIntervalDeletionSuccess() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson(validPerson);


        // Person has time interval to be deleted
        ArrayList<TimeInterval> validTimeInterval = new ArrayList<>();
        validTimeInterval.add(ParserUtil.parseEachInterval(VALID_TIME_MON));
        CommandResult commandResult = new DeletePersonTimeCommand(validPerson.getName(), validTimeInterval).execute(modelStub);

        assertEquals(String.format(DeleteTimeCommand.MESSAGE_DELETE_TIME_SUCCESS, validPerson.getName()),
                commandResult.getFeedbackToUser());
        // Time interval has been deleted
        assertEquals(false, modelStub.hasTime(ParserUtil.parseEachInterval(VALID_TIME_MON)));
    }

    @Test
    public void execute_personSingleTimeIntervalDeletionFail() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson(validPerson);
        // Person does not have the time interval
        ArrayList<TimeInterval> invalidTimeInterval = new ArrayList<>();
        invalidTimeInterval.add(ParserUtil.parseEachInterval(VALID_TIME_TUE));
        DeletePersonTimeCommand failedCommand = new DeletePersonTimeCommand(validPerson.getName(), invalidTimeInterval);

        assertThrows(CommandException.class, "These times are not in the list:\n" + "TUE 1300 - TUE 1400 \n",
                () -> failedCommand.execute(modelStub));

        // Time interval has not been deleted
        assertEquals(true, modelStub.hasTime(ParserUtil.parseEachInterval(VALID_TIME_MON)));
    }

    @Test
    public void execute_personMultipleTimeIntervalDeletionFail() throws Exception {
        ModelStubWithPerson modelStub = new ModelStubWithPerson(validPerson);
        // Person does not have the time interval
        ArrayList<TimeInterval> invalidTimeInterval = new ArrayList<>();
        invalidTimeInterval.add(ParserUtil.parseEachInterval(VALID_TIME_MON));
        invalidTimeInterval.add(ParserUtil.parseEachInterval(VALID_TIME_TUE));
        DeletePersonTimeCommand failedCommand = new DeletePersonTimeCommand(validPerson.getName(), invalidTimeInterval);

        assertThrows(CommandException.class, "These times are not in the list:\n" + "TUE 1300 - TUE 1400 \n"
                + "The other times have been deleted\n",
                () -> failedCommand.execute(modelStub));

        // Time interval has been deleted
        assertEquals(false, modelStub.hasTime(ParserUtil.parseEachInterval(VALID_TIME_MON)));
        // Time interval which was not there is stil not there
        assertEquals(false, modelStub.hasTime(ParserUtil.parseEachInterval(VALID_TIME_TUE)));
    }

    @Test
    public void execute_personMultipleTimeIntervalDeletionPass() throws Exception {
        ModelStubPersonWithMultipleTimings modelStub = new ModelStubPersonWithMultipleTimings(validPerson);
        // Person has all the time intervals
        ArrayList<TimeInterval> validTimeInterval = new ArrayList<>();
        validTimeInterval.add(ParserUtil.parseEachInterval(VALID_TIME_MON));
        validTimeInterval.add(ParserUtil.parseEachInterval(VALID_TIME_TUE));
        DeletePersonTimeCommand failedCommand = new DeletePersonTimeCommand(validPerson.getName(), validTimeInterval);

        assertEquals(String.format(DeleteTimeCommand.MESSAGE_DELETE_TIME_SUCCESS, validPerson.getName()),
                failedCommand.execute(modelStub).getFeedbackToUser());

        // Time interval has been deleted
        assertEquals(false, modelStub.hasTime(ParserUtil.parseEachInterval(VALID_TIME_MON)));
        // Time interval has been deleted
        assertEquals(false, modelStub.hasTime(ParserUtil.parseEachInterval(VALID_TIME_TUE)));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Name personName) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Person deletePerson(String personName) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Group> getFilteredGroupList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredGroupList(Predicate<Group> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addGroup(Group g) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Group deleteGroup(String groupName) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Pair<Person, Group> groupPerson(String personName, String groupName) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Pair<Person, Group> ungroupPerson(String personName, String groupName) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Group addGroupRemark(String groupName, GroupRemark groupRemark) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String addTimeToPerson(Name toAddPerson, ArrayList<TimeInterval> toAddFreeTime) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasGroup(Group group) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public TimeIntervalList getTimeFromPerson(Name personName) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String deleteTimeFromPerson(Name personName, ArrayList<TimeInterval> listOfTimesToDelete) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Group findGroup(String groupName) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String addTimeToGroup(Group toAdd, ArrayList<TimeInterval> toAddTime) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String deleteTimeFromGroup(Group group,
                                        ArrayList<TimeInterval> toDeleteTime) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        public TimeIntervalList getTimeFromGroup(Group group) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPhone(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasEmail(Person person) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person and a time interval.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) throws ParseException {
            requireNonNull(person);
            this.person = person;
            this.person.addFreeTime(ParserUtil.parseEachInterval(VALID_TIME_MON));
        }

        public boolean hasPerson(Name person) {
            requireNonNull(person);
            return this.person.getName().equals(person);
        }

        public boolean hasTime(TimeInterval timeInterval) {
            requireNonNull(timeInterval);
            return this.person.hasFreeTime(timeInterval);
        }

        @Override
        public String deleteTimeFromPerson(Name personName,
                                         ArrayList<TimeInterval> toDeleteTime) throws CommandException {
            requireNonNull(personName);
            Person person = this.person;
            try {
                return person.deleteFreeTime(toDeleteTime);
            } catch (CommandException e) {
                throw new CommandException(e.getMessage());
            }
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubPersonWithMultipleTimings extends ModelStub {
        private final Person person;

        ModelStubPersonWithMultipleTimings(Person person) throws ParseException {
            requireNonNull(person);
            this.person = person;
            this.person.addFreeTime(ParserUtil.parseEachInterval(VALID_TIME_MON));
            this.person.addFreeTime(ParserUtil.parseEachInterval(VALID_TIME_TUE));
        }

        public boolean hasPerson(Name person) {
            requireNonNull(person);
            return this.person.getName().equals(person);
        }

        public boolean hasTime(TimeInterval timeInterval) {
            requireNonNull(timeInterval);
            return this.person.hasFreeTime(timeInterval);
        }

        @Override
        public String deleteTimeFromPerson(Name personName,
                                         ArrayList<TimeInterval> toDeleteTime) throws CommandException {
            requireNonNull(personName);
            Person person = this.person;
            try {
                return person.deleteFreeTime(toDeleteTime);
            } catch (CommandException e) {
                throw new CommandException(e.getMessage());
            }
        }
    }

}
