package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

import java.util.ArrayList;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddGroupMeetingTimeCommand;
import seedu.address.logic.commands.AddTimeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.TimeInterval;
import seedu.address.model.group.Group;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new AddGroupFreeTimeCommand object
 */
public class AddGroupMeetingTimeCommandParser implements Parser<AddGroupMeetingTimeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddGroupFreeTimeCommand
     * and returns a AddGroupFreeTimeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddGroupMeetingTimeCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_GROUPTAG, PREFIX_FREETIME);

        if (!arePrefixesPresent(argMultimap, PREFIX_GROUPTAG, PREFIX_FREETIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGroupMeetingTimeCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_GROUPTAG);
        Group group = ParserUtil.parseSingleGroup(argMultimap.getValue(PREFIX_GROUPTAG).get());
        ArrayList<TimeInterval> timeInterval = ParserUtil.parseInterval(argMultimap.getAllValues(PREFIX_FREETIME));

        if (TimeInterval.isTimeIntervalOverlap(timeInterval)) {
            throw new ParseException(TimeInterval.MESSAGE_CONSTRAINTS_OVERLAP);
        }

        return new AddGroupMeetingTimeCommand(group, timeInterval);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
