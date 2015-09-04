package tomatron.model;

public class Pomodoro extends Chronometer implements IPomodoro  {
	public static int getDefaultWorkDuration() { return 25; }
	public static int getDefaultLongBreakDuration() { return 15; }
	public static int getDefaultShortBreakDuration() { return 5; }
	
	@Override
	public void pause() {
		stop();
	}
}
