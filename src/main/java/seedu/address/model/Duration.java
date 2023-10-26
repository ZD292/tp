package seedu.address.model;

public class Duration {

	private int duration;
	public Duration(int duration) {
		this.duration = duration;
	}

	public int getDurationInMin() {
		return this.duration;
	}

}
