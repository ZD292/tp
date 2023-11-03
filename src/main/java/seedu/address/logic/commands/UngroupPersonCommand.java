package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import javafx.util.Pair;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;

/**
 * Removes a person from a group.
 */
public class UngroupPersonCommand extends Command {

    public static final String COMMAND_WORD = "ungroup";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes a person from a group in the address book.\n"
        + "Parameters: "
        + PREFIX_NAME + "NAME "
        + PREFIX_GROUPTAG + "GROUP ";

    public static final String MESSAGE_SUCCESS = "%1$s is no longer a part of %2$s";
    private final String personName;
    private final String groupName;


    /**
     * Creates an UngroupPersonCommand to add the specified {@code Person}
     * to the specified {@code Group}
     */
    public UngroupPersonCommand(String personName, String groupName) {
        requireNonNull(personName);
        requireNonNull(groupName);
        this.personName = personName;
        this.groupName = groupName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Pair<Person, Group> output = model.ungroupPerson(this.personName, this.groupName);
        Person person = output.getKey();
        Group group = output.getValue();

        return new CommandResult(String.format(MESSAGE_SUCCESS, person.getName().fullName, group.getGroupName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof GroupPersonCommand)) {
            return false;
        }

        UngroupPersonCommand otherUngroupPersonCommand = (UngroupPersonCommand) other;
        return this.equals(otherUngroupPersonCommand);

    }

    // to fix
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("toRemoveFromGroup", "")
            .toString();
    }
}
