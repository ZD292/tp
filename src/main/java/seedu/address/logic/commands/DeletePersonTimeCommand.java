package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.TimeInterval;
import seedu.address.model.person.Name;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;

public class DeletePersonTimeCommand extends DeleteTimeCommand {
    private final ArrayList<TimeInterval> timeIntervalsToDelete;
    private final Name personName;

    /**
     * Creates a DeletePersonTimeCommand to Delete the specified {@code timeIntervalsToDelete}
     */
    public DeletePersonTimeCommand(Name personName, ArrayList<TimeInterval> timeIntervalsToDelete) {
        requireNonNull(personName);
        requireNonNull(timeIntervalsToDelete);
        this.personName = personName;
        this.timeIntervalsToDelete = timeIntervalsToDelete;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        String status;
        if (model.hasPerson(personName)) {
           status = model.deleteTimeFromPerson(personName, timeIntervalsToDelete);
        } else {
            throw new CommandException("Person does not exist \n");
        }
        return new CommandResult(String.format(MESSAGE_DELETE_TIME_SUCCESS + status, personName.fullName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeletePersonTimeCommand)) {
            return false;
        }

        DeletePersonTimeCommand otherDeletePersonTimeCommand = (DeletePersonTimeCommand) other;
        if (timeIntervalsToDelete.size() != otherDeletePersonTimeCommand.timeIntervalsToDelete.size()) {
            return false;
        }
        boolean isSameArray = true;
        for (int i = 0; i < timeIntervalsToDelete.size(); i++) {
            isSameArray = isSameArray && timeIntervalsToDelete.get(i).equals(
                    otherDeletePersonTimeCommand.timeIntervalsToDelete.get(i));
        }
        return personName.equals(otherDeletePersonTimeCommand.personName)
                && isSameArray;
    }

}
