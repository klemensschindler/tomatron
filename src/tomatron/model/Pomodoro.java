package tomatron.model;

public class Pomodoro extends Chronometer implements IPomodoro  {

	private int shortPause;
	private int longPause;

	public void setShortPause(int minutes) {
		this.shortPause = minutes * 60;

	}

	public void setLongPause(int minutes) {
		this.longPause = minutes * 60;
	}
}
