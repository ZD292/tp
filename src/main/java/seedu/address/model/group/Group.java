package seedu.address.model.group;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Duration;
import seedu.address.model.TimeIntervalList;
import seedu.address.model.TimeInterval;
import seedu.address.model.person.Person;

import java.util.stream.Stream;

/**
 * Class representing a group
 */
public class Group {
    public static final String MESSAGE_CONSTRAINTS = "Group names should be alphanumeric and must not be blank";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";
    private final ObservableList<Person> listOfGroupMates = FXCollections.observableArrayList();
    private final String groupName;
    private GroupRemark groupRemark;
    private final TimeIntervalList timeIntervalList = new TimeIntervalList();

    /**
     * Name field must be present and not null.
     */
    public Group(String groupName) {
        requireNonNull(groupName);
        this.groupName = groupName;
        this.groupRemark = new GroupRemark("");
    }

    public Group(String groupName, GroupRemark groupRemark) {
        requireNonNull(groupName);
        this.groupName = groupName;
        this.groupRemark = groupRemark;
    }

    /**
     * Name field must be present and not null.
     */
    public Group(String groupName, GroupRemark groupRemark, TimeIntervalList timeIntervalList) {
        requireNonNull(groupName);
        this.groupName = groupName;
        this.groupRemark = groupRemark;
        this.timeIntervalList.addAll(timeIntervalList);
    }

    /**
     * Name field and listOfGroupMates must be present and not null.
     */
    public Group(String groupName, GroupRemark groupRemark, List<Person> listOfGroupMates) {
        requireNonNull(groupName);
        requireNonNull(listOfGroupMates);
        this.groupName = groupName;
        this.groupRemark = groupRemark;
        this.listOfGroupMates.addAll(listOfGroupMates);
    }

    /**
     * Name field, listOfGroupMates and timeIntervalList must be present and not null.
     */
    public Group(String groupName, GroupRemark groupRemark, List<Person> listOfGroupMates, TimeIntervalList timeIntervalList) {
        requireNonNull(groupName);
        requireNonNull(listOfGroupMates);
        requireNonNull(timeIntervalList);
        this.groupName = groupName;
        this.groupRemark = groupRemark;
        this.timeIntervalList.addAll(timeIntervalList);
        this.listOfGroupMates.addAll(listOfGroupMates);
    }

    public String getGroupName() {
        return groupName;
    }

    public TimeIntervalList getTimeIntervalList() {
        return timeIntervalList;
    }
    /**
     * Converts the internal list to streams.
     *
     * @return Internal list into streams.
     */
    public Stream<Person> toStream() {
        return this.listOfGroupMates.stream();
    }

    /**
     * Returns true if both groups have the same name.
     * This defines a weaker notion of equality between two groups.
     */
    public boolean isSameGroup(Group otherGroup) {
        return this.equals(otherGroup);
    }

    /**
     * Check if same group according to name since groupName is unique
     *
     * @param groupName of interest
     * @return whether group is the same group
     */
    public boolean nameEquals(String groupName) {
        return this.groupName.equals(groupName);
    }

    /**
     * Returns if the name of the group is valid.
     *
     * @param name The name of the group
     * @return The validity of the group name.
     */
    //For now no constraints
    public static boolean isValidGroup(String name) {
        requireNonNull(name);

        return !name.isBlank() && name.matches(VALIDATION_REGEX);
    }

    /**
     * Removes the person from the group.
     * The person must exist in the group.
     */
    public void removePerson(Person toRemove) throws CommandException {
        requireNonNull(toRemove);
        if (!this.listOfGroupMates.contains(toRemove)) {
            throw new CommandException(
                String.format("%s is not in this group: %s", toRemove.getName().fullName, this.groupName));
        }
        listOfGroupMates.remove(toRemove);
    }

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(Person toCheck) {
        requireNonNull(toCheck);
        return listOfGroupMates.stream().anyMatch(toCheck::isSamePerson);
    }

    /**
     * Adds a person to the list.
     * The person must not already exist in the list.
     */
    public void addPerson(Person personToAdd) throws CommandException {
        requireNonNull(personToAdd);
        if (this.contains(personToAdd)) {
            throw new CommandException(
                String.format("%s is already in this group: %s", personToAdd.getName().fullName, this.groupName));
        }
        listOfGroupMates.add(personToAdd);
    }

    public ObservableList<Person> getListOfGroupMates() {
        return this.listOfGroupMates;
    }

    public GroupRemark getGroupRemark() {
        return this.groupRemark;
    }

    public void setGroupRemark(GroupRemark groupRemark) {
        this.groupRemark = groupRemark;
    }

    /**
     * Modify StringBuilder to display message should any groupMate not input their free time
     * @param br StringBuilder
     * @param format Format specifier
     */
    public void areAllFree(StringBuilder br, String format) {
        for (Person p: this.listOfGroupMates) {
            if (p.isNotFree()) {
                br.append(String.format(format, p.getName().fullName));
            }
        }
    }

    /**
     * Compare each person in group to get overlap
     * Accumulate the result
     * @param duration represent duration in minutes
     * @return TimeInterval that can fit duration specified
     */
    public TimeIntervalList findFreeTime(Duration duration) throws CommandException {
        // compare person to person get overlap
        // will use one getter for freeTime
        TimeIntervalList freeTime = new TimeIntervalList();
        // nobody in group
        if (listOfGroupMates.isEmpty()) {
            throw new CommandException("Group is empty");
        }
        // only 1 person in group
        if (listOfGroupMates.size() == 1) {
            Person person = listOfGroupMates.get(0);
            TimeIntervalList personFreeTime = person.getTime();
            // check whether fits duration or not
            personFreeTime = personFreeTime.fitDuration(duration);
            return personFreeTime;
        }

        for (int i = 0; i < listOfGroupMates.size(); i++) {
           if (i + 1 == listOfGroupMates.size()) {
               break;
           }
           Person firstPerson = listOfGroupMates.get(i);
           Person secondPerson = listOfGroupMates.get(i + 1);
           TimeIntervalList first = firstPerson.getTime();
           TimeIntervalList second = secondPerson.getTime();
           // uninitialised freeTime e.g. first and second person in group
           if (freeTime.isEmpty()) {
               freeTime = first.findOverlap(second, duration);
           } else {
               freeTime = freeTime.findOverlap(second, duration);
           }
        }
        return freeTime;
    }

    /**
     * Adds a single time interval to the group
     * @param toAddTime time interval to add
     */
    public void addTime(TimeInterval toAddTime) throws CommandException {
        this.timeIntervalList.addTime(toAddTime);
    }

    /**
     * Adds a list of free time to the group
     * @param toAddTime List of time intervals to add
     * @throws CommandException When there is a clash in timings within the list
     */
    public String addTime(ArrayList<TimeInterval> toAddTime) throws CommandException {
        return this.timeIntervalList.addTime(toAddTime);
    }

    /**
     * Checks if the group has the time interval
     * @param timeInterval time interval to check
     * @return result of check
     */
    public boolean hasTime(TimeInterval timeInterval) {
        return this.timeIntervalList.hasTime(timeInterval);
    }

    public TimeIntervalList getTime() {
        return this.timeIntervalList;
    }

    public String deleteTime(ArrayList<TimeInterval> toDeleteTime) throws CommandException { return this.timeIntervalList.deleteTime(toDeleteTime);}

    @Override
    public boolean equals(Object group) {
        if (group == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(group instanceof Group)) {
            return false;
        }

        Group otherGroup = (Group) group;
        return this.groupName.equals(otherGroup.getGroupName());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Group name", groupName)
                .toString();
    }

    public int size() {
        return listOfGroupMates.size();
    }
}
