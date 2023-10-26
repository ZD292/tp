package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPREMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPTAG;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupRemark;

/**
 * Changes the remark of an existing person in the address book.
 */
public class GroupRemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the remark of the group identified "
            + "by the name of the group. "
            + "Existing remark will be overwritten by the input.\n"
            + "Parameters: "
            + PREFIX_GROUPTAG + "GROUPNAME "
            + PREFIX_GROUPREMARK + "REMARK\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_GROUPTAG + "CS2103T "
            + PREFIX_GROUPREMARK + "Finals on 30 Nov";

    public static final String MESSAGE_SUCCESS = "Added remark to group %1$s: %2$s";
    private final String groupName;
    private final GroupRemark groupRemark;

    /**
     * @param groupName of the group to edit the remark
     * @param groupRemark of the group to be updated to
     */
    public GroupRemarkCommand(String groupName, GroupRemark groupRemark) {
        requireAllNonNull(groupName, groupRemark);

        this.groupName = groupName;
        this.groupRemark = groupRemark;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Group editedGroup = model.addGroupRemark(this.groupName, this.groupRemark);
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedGroup.getGroupName(), editedGroup.getGroupRemark()));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GroupRemarkCommand)) {
            return false;
        }

        // state check
        GroupRemarkCommand e = (GroupRemarkCommand) other;
        return groupRemark.equals(e.groupRemark)
                && groupRemark.equals(e.groupName);
    }
}