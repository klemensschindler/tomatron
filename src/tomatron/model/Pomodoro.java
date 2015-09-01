package tomatron.model;
import java.util.Timer;	

public class Pomodoro extends Chronometer implements IPomodoro  {

	private int shortPause;
	private int longPause;

	public void setShortPause(int minutes) {
		this.shortPause = minutes;

	}
	public void setLongPause(int minutes) {
		this.longPause = minutes;
	}


	//public enum PomodoroState {
		//inactive, pomodoro, shortBreak, longBreak
	//}

	//private PomodoroState state = PomodoroState.inactive;
	//int secondsRemaining = 0;
	//Timer timer = new Timer();
}
