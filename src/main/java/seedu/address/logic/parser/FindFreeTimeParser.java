package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUPTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DURATION;

import java.util.stream.Stream;

import seedu.address.logic.commands.FindFreeTimeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Duration;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class FindFreeTimeParser implements Parser<FindFreeTimeCommand> {

	/**
	 * Parses the given {@code String} of arguments in the context of the AddCommand
	 * and returns an AddCommand object for execution.
	 *
	 * @throws ParseException if the user input does not conform the expected format
	 */

	public FindFreeTimeCommand parse(String args) throws ParseException {
		String trimmedArgs = args.trim();
		if (trimmedArgs.isEmpty()) {
			throw new ParseException(
					String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindFreeTimeCommand.MESSAGE_USAGE));
		}

//		return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
		ArgumentMultimap argMultimap =
				ArgumentTokenizer.tokenize(args, PREFIX_DURATION, PREFIX_GROUPTAG);

		if (!arePrefixesPresent(argMultimap, PREFIX_DURATION, PREFIX_GROUPTAG) || !argMultimap.getPreamble().isEmpty()) {
			throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindFreeTimeCommand.MESSAGE_USAGE));
		}
		String groupName = argMultimap.getValue(PREFIX_GROUPTAG).get();
		String durationName = argMultimap.getValue(PREFIX_DURATION).get();
		int duration = 0;

		try {
			duration = Integer.parseInt(durationName);
			if (duration == 0) {
				throw new IllegalArgumentException("You can't have a meeting without specifying a duration");
			}
			if (duration < 0) {
				throw new IllegalArgumentException("Duration specified is less than 0");
			}
			// there are 10079 mins from mon 0000 to sun 1159
			if (duration > 10079) {
				throw new IllegalArgumentException(String.format("The value you entered, %d is beyond the time you have in a week", duration));
			}
		} catch (NumberFormatException e) {
			throw new ParseException(
					String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindFreeTimeCommand.MESSAGE_DURATION_USAGE));
		} catch (IllegalArgumentException i) {
			throw new ParseException(
					String.format(MESSAGE_INVALID_COMMAND_FORMAT, i.getMessage()));
		}
		// 0 is duration placeholder, should not enter this line if duration is 0
		return new FindFreeTimeCommand(groupName, new Duration(duration));
	}

	/**
	 * Returns true if none of the prefixes contains empty {@code Optional} values in the given
	 * {@code ArgumentMultimap}.
	 */
	private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
		return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
	}


}
