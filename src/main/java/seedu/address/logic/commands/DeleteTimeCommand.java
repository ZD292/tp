package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

import static seedu.address.logic.parser.CliSyntax.*;

/**
 * Deletes time from the individual and group.
 */
public abstract class DeleteTimeCommand extends Command {
    public static final String MESSAGE_DELETE_TIME_SUCCESS = "Deleted Time from: %1$s";
    public static final String COMMAND_WORD = "deletetime";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes free time from an existing person or group. \n"
            + "For person, parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_FREETIME + "FREE TIME \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Alex Yeoh "
            + PREFIX_FREETIME + "mon 1200 - mon 1400 " + PREFIX_FREETIME  + "tue 1000 - wed 1600 \n"
            + "For group, parameters: "
            + PREFIX_GROUPTAG + "GROUPNAME "
            + PREFIX_FREETIME + "FREE TIME \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_GROUPTAG + "CS2103T "
            + PREFIX_FREETIME + "mon 1200 - mon 1400 " + PREFIX_FREETIME  + "tue 1000 - wed 1600";

    @Override
    public abstract CommandResult execute(Model model) throws CommandException;


}
