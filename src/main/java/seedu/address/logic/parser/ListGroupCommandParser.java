package seedu.address.logic.parser;

import seedu.address.logic.commands.ListGroupCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListGroupCommandParser implements Parser<ListGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListGroupCommand
     * and returns an ListGroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ListGroupCommand parse(String args) throws ParseException {
        if (args.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListGroupCommand.MESSAGE_USAGE));
        }

        return new ListGroupCommand();
    }
}
