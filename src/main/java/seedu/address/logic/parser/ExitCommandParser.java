package seedu.address.logic.parser;

import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

/**
 * Parses input arguments and creates a new ExitCommand object
 */
public class ExitCommandParser implements Parser<ExitCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ExitCommand
     * and returns an ExitCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ExitCommand parse(String args) throws ParseException {
        if (args.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExitCommand.MESSAGE_USAGE));
        }

        return new ExitCommand();
    }
}
