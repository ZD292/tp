package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.*;

import javafx.util.Pair;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Duration;
import seedu.address.model.FreeTime;
import seedu.address.model.TimeIntervalList;
import seedu.address.model.group.Group;
import seedu.address.model.Model;
import seedu.address.model.group.exceptions.GroupNotFoundException;
import seedu.address.model.person.Person;

/**
 * Adds a person to a group.
 */
public class FindFreeTimeCommand extends Command {

	public static final String COMMAND_WORD = "findfreetime";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds a free time slot given a group and duration \n" +
			"Parameters: " + PREFIX_GROUPTAG + "GROUPNAME " + PREFIX_DURATION + "DURATION";

	public static final String MESSAGE_DURATION_USAGE = "Enter Duration in minutes";


	public static final String MESSAGE_NOT_ALL_FREE = "%s has not input their free time yet\n";

	public static final String MESSAGE_SUCCESS = "These are the available timeslots \n";
	public static final String MESSAGE_INTERVAL_DISPLAY = "%d. %s\n";

	private final String groupName;
	private final Duration duration;


	/**
	 * Creates an AddCommand to add the specified {@code Person}
	 */
	public FindFreeTimeCommand(String groupName, Duration duration) {
		requireNonNull(groupName);
		requireNonNull(duration);
		this.groupName = groupName;
		this.duration = duration;
	}

	@Override
	public CommandResult execute(Model model) throws CommandException {
		// 3 steps
		// find group, if group exists check everybody input time, use find free time algo
		requireNonNull(model);
		Group g;
		// br stores message
		StringBuilder br = new StringBuilder();
		int intervalCounter = 1;
		try {
			g = model.findGroup(groupName);
		} catch (GroupNotFoundException e) {
			throw new CommandException(e.getMessage());
		}

		// check everybody input time, modify br should somebody not key in their free time
		g.areAllFree(br, MESSAGE_NOT_ALL_FREE);
		if (br.length() != 0) {
			throw new CommandException(br.toString());
		}

		// use algorithm to findFreeTime()
		TimeIntervalList freeTime = g.findFreeTime(duration);
		freeTime.getMessage(br, MESSAGE_INTERVAL_DISPLAY);

		return new CommandResult(MESSAGE_SUCCESS + br.toString());
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}

		// instanceof handles nulls
		if (!(other instanceof GroupPersonCommand)) {
			return false;
		}

		GroupPersonCommand otherGroupPersonCommand = (GroupPersonCommand) other;
		// to check
		return this.equals(otherGroupPersonCommand);

	}

	// to fix
	@Override
	public java.lang.String toString() {
		return new ToStringBuilder(this)
				.add("toAddToGroup", "")
				.toString();
	}
}

