package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.util.Pair;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupRemark;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Group> filteredGroups;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);
        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredGroups = new FilteredList<>(this.addressBook.getGroupList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    //=========== Person functions ===========================================================================

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public Person deletePerson(String personName) throws CommandException {
        Person person = addressBook.getPerson(personName);
        addressBook.removePerson(person);
        return person;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public boolean hasPerson(Name personName) {
        requireNonNull(personName);
        return addressBook.hasPerson(personName);
    }

    /**
     * Assign person to group
     *
     * @param person person to be grouped
     * @param group  group in consideration
     * @throws CommandException if person has already been assigned to group
     */
    private void assignGroup(Person person, Group group) throws CommandException {
        group.addPerson(person);
        person.addGroup(group);
    }

    /**
     * Unassign person to group
     *
     * @param person person to be grouped
     * @param group  group in consideration
     * @throws CommandException if person has already been assigned to group
     */
    private void unassignGroup(Person person, Group group) throws CommandException {
        group.removePerson(person);
        person.removeGroup(group);
    }

    @Override
    public Pair<Person, Group> groupPerson(String personName, String groupName) throws CommandException {
        // both throw exception if not exists exact match
        Person person = addressBook.getPerson(personName);
        Group group = addressBook.getGroup(groupName);
        this.assignGroup(person, group);
        forceUpdateList();
        Pair<Person, Group> output = new Pair<>(person, group);
        return output;
    }

    @Override
    public Pair<Person, Group> ungroupPerson(String personName, String groupName) throws CommandException {
        Person person = addressBook.getPerson(personName);
        Group group = addressBook.getGroup(groupName);
        this.unassignGroup(person, group);
        forceUpdateList();
        Pair<Person, Group> output = new Pair<>(person, group);
        return output;
    }

    @Override
    public String addTimeToPerson(Name toAddPerson, ArrayList<TimeInterval> toAddTime) throws CommandException {
        requireNonNull(toAddPerson);
        Person person = addressBook.getPerson(toAddPerson.fullName);
        try {
            String msg = person.addFreeTime(toAddTime);
            forceUpdateList();
            return msg;
        } catch (CommandException e) {
            throw new CommandException(e.getMessage());
        }
    }

    /**
     * Deletes free time from person
     * @param personName Person to delete time from
     * @param toDeleteTime Time to be deleted
     * @throws CommandException if time does not exist
     */
    @Override
    public void deleteTimeFromPerson(Name personName,
                                     ArrayList<TimeInterval> toDeleteTime) throws CommandException {
        requireNonNull(personName);
        Person person = addressBook.getPerson(personName.fullName);
        try {
            person.deleteFreeTime(toDeleteTime);
        } catch (CommandException e) {
            throw new CommandException(e.getMessage());
        }
        forceUpdateList();
    }

    public TimeIntervalList getTimeFromPerson(Name personName) throws CommandException {
        requireNonNull(personName);
        Person person = addressBook.getPerson(personName.toString());
        return person.getTime();
    }

    //=========== Group functions ============================================================================

    /**
     * Adds a group to the address book.
     * The group must not already exist in the address book.
     */
    public void addGroup(Group group) {
        addressBook.addGroup(group);
    }

    /**
     * Removes {@code group} from this {@code AddressBook}.
     * {@code group} must exist in the address book.
     */
    public Group deleteGroup(String groupName) throws CommandException {
        Group group = addressBook.getGroup(groupName);
        addressBook.removeGroup(group);
        forceUpdateList();
        return group;
    }

    /**
     * Returns true if a group with the same identity as {@code group} exists in the address book.
     */
    public boolean hasGroup(Group group) {
        requireNonNull(group);
        return addressBook.hasGroup(group);
    }

    public Group findGroup(String groupName) throws CommandException {
        return addressBook.getGroup(groupName);
    }

    public String addTimeToGroup(Group toAdd, ArrayList<TimeInterval> toAddTime) throws CommandException {
        requireNonNull(toAdd);
        Group groupToAdd = addressBook.getGroup(toAdd.getGroupName());
        try {
            String status = groupToAdd.addTime(toAddTime);
            forceUpdateList();
            return status;
        } catch (CommandException e) {
            throw new CommandException(e.getMessage());
        }
    }

    /**
     * Deletes meeting time from group
     * @param group Group to delete time from
     * @param toDeleteTime Time to be deleted
     * @throws CommandException if time does not exist
     */
    @Override
    public void deleteTimeFromGroup(Group group,
                                    ArrayList<TimeInterval> toDeleteTime) throws CommandException {
        requireNonNull(group);
        Group groupToDeleteTime = addressBook.getGroup(group.getGroupName());
        try {
            groupToDeleteTime.deleteTime(toDeleteTime);
        } catch (CommandException e) {
            forceUpdateList();
            throw new CommandException(e.getMessage());
        }
        forceUpdateList();
    }

    public Group addGroupRemark(String groupName, GroupRemark groupRemark) throws CommandException {
        Group group = addressBook.getGroup(groupName);
        group.setGroupRemark(groupRemark);
        return group;
    }

    public TimeIntervalList getTimeFromGroup(Group group) throws CommandException {
        requireNonNull(group);
        Group toAdd = addressBook.getGroup(group.getGroupName());
        return toAdd.getTime();
    }


    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    /**
     * Returns an unmodifiable view of the list of {@code Group} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Group> getFilteredGroupList() {
        return filteredGroups;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public void updateFilteredGroupList(Predicate<Group> predicate) {
        requireNonNull(predicate);
        filteredGroups.setPredicate(predicate);
    }

    @Override
    public boolean hasEmail(Person toAdd) {
        return addressBook.hasEmail(toAdd);
    }

    @Override
    public boolean hasPhone(Person toAdd) {
        return addressBook.hasPhone(toAdd);
    }

    private void forceUpdateList() {
        updateFilteredPersonList(user -> false);
        updateFilteredPersonList(user -> true);
        updateFilteredGroupList(group -> false);
        updateFilteredGroupList(group -> true);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
            && userPrefs.equals(otherModelManager.userPrefs)
            && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
