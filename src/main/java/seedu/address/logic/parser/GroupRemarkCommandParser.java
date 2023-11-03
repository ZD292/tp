package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPREMARK;

import seedu.address.logic.commands.GroupRemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.group.GroupRemark;

import java.util.stream.Stream;

/**
 * Parses input arguments and creates a new {@code RemarkCommand} object
 */
public class GroupRemarkCommandParser implements Parser<GroupRemarkCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code RemarkCommand}
     * and returns a {@code RemarkCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public GroupRemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GROUPTAG, PREFIX_GROUPREMARK);

        if (!arePrefixesPresent(argMultimap, PREFIX_GROUPTAG, PREFIX_GROUPREMARK)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, GroupRemarkCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_GROUPTAG, PREFIX_GROUPREMARK);

        String groupName = argMultimap.getValue(PREFIX_GROUPTAG).get();
        String groupRemark = argMultimap.getValue(PREFIX_GROUPREMARK).orElse("");

        return new GroupRemarkCommand(groupName, new GroupRemark(groupRemark));
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}