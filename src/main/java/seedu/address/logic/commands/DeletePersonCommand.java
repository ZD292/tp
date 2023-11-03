package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.GroupList;
import seedu.address.model.person.Person;

/**
 * Deletes person from the addressbook and group.
 */
public class DeletePersonCommand extends DeleteCommand {
    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    private final String personName;

    public DeletePersonCommand(String personName) {
        this.personName = personName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person personToDelete = model.deletePerson(this.personName);

        //Delete person from all groups
        GroupList personGroups = personToDelete.getGroups();
        personGroups.toStream().forEach(g -> {
            model.getAddressBook().getGroupList().stream().forEach(f -> {
                try {
                    if (f.equals(g)) {
                        f.removePerson(personToDelete);
                    }
                } catch (CommandException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete.getName().fullName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeletePersonCommand)) {
            return false;
        }

        DeletePersonCommand otherDeletePersonCommand = (DeletePersonCommand) other;
        return personName.equals(otherDeletePersonCommand.personName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("name", personName)
            .toString();
    }
}
