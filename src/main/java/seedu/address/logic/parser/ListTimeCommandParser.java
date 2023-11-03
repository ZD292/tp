package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.logic.commands.*;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.group.Group;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new ListTimeCommand object
 */
public class ListTimeCommandParser implements Parser<ListTimeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListTimeCommand
     * and returns a ListTimeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListTimeCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListTimeCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GROUPTAG);

        if (args.length() < 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListTimeCommand.MESSAGE_USAGE));
        }

        // check if either n/ or g/ are present
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            if (!arePrefixesPresent(argMultimap, PREFIX_GROUPTAG)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListTimeCommand.MESSAGE_USAGE));
            }
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_GROUPTAG);

        // if n/ is present
        if (arePrefixesPresent(argMultimap, PREFIX_NAME)) {
            // check if g/ is present
            if (arePrefixesPresent(argMultimap, PREFIX_GROUPTAG)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListTimeCommand.MESSAGE_USAGE));
            } else {
                String personName = argMultimap.getValue(PREFIX_NAME).get();
                Name name = ParserUtil.parseName(personName);
                return new ListTimePersonCommand(name);
            }
        }


        // n/ not present, g/ should be present
        String groupName = argMultimap.getValue(PREFIX_GROUPTAG).get();
        Group group = ParserUtil.parseSingleGroup(groupName);
        return new ListTimeGroupCommand(group);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
