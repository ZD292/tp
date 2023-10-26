package seedu.address.logic.commands;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * Finds and lists all persons in address book who is inside the specified Group.
 * Displays group remarks of the specified group.
 * Keyword matching is case sensitive.
 */
public class FindGroupCommand extends FindCommand {

    public static final String MESSAGE_GROUP_FOUND = "All persons from Group %1$s listed!\n"
            + "Group remarks for %1$s: \n"
            + "%2$s";
    private final String groupName;

    /**
     * @param groupName of the group to find the groupmates and remark of
     */
    public FindGroupCommand(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Group groupToFind = model.findGroup(this.groupName);

        //List all people in group
        Predicate<Person> inGroupPred = (p) -> p.containsGroup(groupToFind);
        model.updateFilteredPersonList(inGroupPred);

        //Display group remark
        return new CommandResult(
                java.lang.String.format(MESSAGE_GROUP_FOUND,
                        groupToFind.getGroupName(), groupToFind.getGroupRemark()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindGroupCommand)) {
            return false;
        }

        FindGroupCommand otherFindGroupCommand = (FindGroupCommand) other;
        return groupName.equals(otherFindGroupCommand.groupName);
    }

    @Override
    public java.lang.String toString() {
        return new ToStringBuilder(this)
                .add("group name", groupName)
                .toString();
    }
}
