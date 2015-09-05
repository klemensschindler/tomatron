package tomatron.model;

public class Pomodoro extends Chronometer implements IPomodoro  {
	
	@Override
	public void pause() {
		stop();
	}
}
