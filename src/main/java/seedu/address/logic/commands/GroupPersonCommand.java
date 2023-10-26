package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import javafx.util.Pair;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.group.Group;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to a group.
 */
public class GroupPersonCommand extends Command {

    public static final String COMMAND_WORD = "group";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to a group in " +
        "the address book.\n" + "Parameters: " + PREFIX_NAME + "NAME " + PREFIX_GROUPTAG + "GROUP ";

    public static final String MESSAGE_SUCCESS = "%1$s is now a part of %2$s";
    public static final String MESSAGE_DUPLICATE_PERSON_IN_GROUP =
        "Error, invalid input entered, unable to put the person into group";

    private final String personName;
    private final String groupName;


    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public GroupPersonCommand(String personName, String groupName) {
        requireNonNull(personName);
        requireNonNull(groupName);
        this.personName = personName;
        this.groupName = groupName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Pair<Person, Group> output = model.groupPerson(this.personName, this.groupName);
        Person person = output.getKey();
        Group group = output.getValue();

        // person already in the group
        System.out.println(person.toString());

        return new CommandResult(String.format(MESSAGE_SUCCESS, person.getName().fullName, group.getGroupName()));
	}

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GroupPersonCommand)) {
            return false;
        }

        GroupPersonCommand otherGroupPersonCommand = (GroupPersonCommand) other;
        // to check
        return this.equals(otherGroupPersonCommand);

    }

    // to fix
    @Override
    public java.lang.String toString() {
        return new ToStringBuilder(this)
            .add("toAddToGroup", "")
            .toString();
    }
}

