package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteGroupCommand;
import seedu.address.logic.commands.DeletePersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GROUPTAG);

        try {
            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_GROUPTAG);
        } catch (ParseException e) {
            throw new ParseException(String.format(DeleteCommand.MESSAGE_TWO_PARAMETERS, DeleteCommand.MESSAGE_USAGE));
        }

        // check if both n/ or g/ are present
        if ((arePrefixesPresent(argMultimap, PREFIX_NAME) && arePrefixesPresent(argMultimap, PREFIX_GROUPTAG))
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(DeleteCommand.MESSAGE_TWO_PARAMETERS, DeleteCommand.MESSAGE_USAGE));
        } else if (arePrefixesPresent(argMultimap, PREFIX_NAME)) {
            String personName = argMultimap.getValue(PREFIX_NAME).get();
            return new DeletePersonCommand(personName);
        } else if (arePrefixesPresent(argMultimap, PREFIX_GROUPTAG)) {
            String groupName = argMultimap.getValue(PREFIX_GROUPTAG).get();
            return new DeleteGroupCommand(groupName);
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
