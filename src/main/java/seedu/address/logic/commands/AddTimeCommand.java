package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FREETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.ArrayList;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.TimeInterval;
import seedu.address.model.person.Name;

/**
 * Adds free time to person.
 */
public class AddTimeCommand extends Command {

    public static final String COMMAND_WORD = "addtime";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds free time to an existing person. \n"
        + "Parameters: "
        + PREFIX_NAME + "NAME "
        + PREFIX_FREETIME + "FREE_TIME \n"
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_NAME + "Alex Yeoh "
        + PREFIX_FREETIME + "mon 1200 - mon 1400 " + PREFIX_FREETIME + "tue 1000 - wed 1600";

    public static final String MESSAGE_SUCCESS = "Free time status for: %1$s \n";


    private final ArrayList<TimeInterval> toAddFreeTimes;
    private final Name toAddPerson;

    /**
     * AddTimeCommand constructor.
     * @param toAddPerson The person object to be added to.
     * @param toAddFreeTimes ArrayList of time intervals to be added to person.
     */
    public AddTimeCommand(Name toAddPerson, ArrayList<TimeInterval> toAddFreeTimes) {
        requireNonNull(toAddPerson);
        requireNonNull(toAddFreeTimes);
        this.toAddFreeTimes = toAddFreeTimes;
        this.toAddPerson = toAddPerson;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        String status;
        if (model.hasPerson(toAddPerson)) {
            status = model.addTimeToPerson(toAddPerson, toAddFreeTimes);
        } else {
            throw new CommandException("Person does not exists");
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS + status, Messages.format(toAddPerson)));
    }
}
