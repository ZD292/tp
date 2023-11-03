package seedu.address.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.util.Pair;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupRemark;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /**
     * Returns the AddressBook
     */
    ReadOnlyAddressBook getAddressBook();

    //=========== Person functions ===========================================================================

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    Person deletePerson(String personName) throws CommandException;

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    boolean hasPerson(Name personName);

    /**
     * Assign person to group and return corresponding person and group object in a pair
     *
     * @param personName String representing person name
     * @param groupName  String representing group name
     * @return Pair representing Person and Group object of interest
     */
    public Pair<Person, Group> groupPerson(String personName, String groupName) throws CommandException;

    /**
     * Unassign group and return corresponding person and group object in a pair
     *
     * @param personName String representing person name
     * @param groupName  String representing group name
     * @return Pair representing Person and Group object of interest
     */
    Pair<Person, Group> ungroupPerson(String personName, String groupName) throws CommandException;

    String addTimeToPerson(Name toAddPerson, ArrayList<TimeInterval> toAddTime) throws CommandException;
    TimeIntervalList getTimeFromPerson(Name personName) throws CommandException;
    String deleteTimeFromPerson(Name personName, ArrayList<TimeInterval> toDeleteTime) throws CommandException;

    //=========== Group functions ============================================================================

    /**
     * Adds a group to the address book.
     * The group must not already exist in the address book.
     */
    public void addGroup(Group g);

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public Group deleteGroup(String groupName) throws CommandException;

    /**
     * Returns true if a group with the same identity as {@code group} exists in the address book.
     */
    public boolean hasGroup(Group group);

    /**
     * Returns group if a group with the same name exists in the address book.
     */
    public Group findGroup(String groupName) throws CommandException;

    Group addGroupRemark(String groupName, GroupRemark groupRemark) throws CommandException;

    String addTimeToGroup(Group toAdd, ArrayList<TimeInterval> toAddTime) throws CommandException;

    String deleteTimeFromGroup(Group group, ArrayList<TimeInterval> toDeleteTime) throws CommandException;

    TimeIntervalList getTimeFromGroup(Group group) throws CommandException;

    //=========== Filtered Person List Accessors =============================================================
    /**
     * Returns an unmodifiable view of the filtered person list
     */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Returns an unmodifiable view of the filtered group list
     */
    ObservableList<Group> getFilteredGroupList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    void updateFilteredGroupList(Predicate<Group> predicate);

    boolean hasEmail(Person toAdd);

    boolean hasPhone(Person toAdd);


}
